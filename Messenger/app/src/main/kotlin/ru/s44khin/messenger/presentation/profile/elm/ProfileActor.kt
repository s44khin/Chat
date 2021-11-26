package ru.s44khin.messenger.presentation.profile.elm

import io.reactivex.Observable
import ru.s44khin.messenger.di.GlobalDI
import ru.s44khin.messenger.domain.LoadProfile
import vivid.money.elmslie.core.ActorCompat

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
                GlobalDI.INSTANCE.profileStore.accept(Event.Ui.LoadProfileNetwork)
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