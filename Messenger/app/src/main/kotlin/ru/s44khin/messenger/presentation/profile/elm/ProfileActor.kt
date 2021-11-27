package ru.s44khin.messenger.presentation.profile.elm

import io.reactivex.Observable
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.domain.LoadProfile
import vivid.money.elmslie.core.ActorCompat
import javax.inject.Inject

class ProfileActor(
    private val loadProfile: LoadProfile
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {

        is Command.LoadProfileNetwork -> loadProfile.fromNetwork()
            .doOnSuccess { loadProfile.saveToDataBase(it) }
            .mapEvents(
                { profile -> Event.Internal.ProfileLoadedNetwork(profile) },
                { error -> Event.Internal.ErrorLoadingNetwork(error) }
            )

        is Command.LoadProfileDB -> loadProfile.fromDataBase()
            .doOnSuccess {
                MessengerApplication.instance.profileComponent.profileStore.accept(Event.Ui.LoadProfileNetwork)
            }
            .mapEvents(
                { profile ->
                    if (profile == null)
                        Event.Internal.ErrorLoadingDataBase(null)
                    else
                        Event.Internal.ProfileLoadedDB(profile)
                },
                { error -> Event.Internal.ErrorLoadingDataBase(error) }
            )
    }
}