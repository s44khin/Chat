package ru.s44khin.messenger.presentation.streams.elm

import io.reactivex.Observable
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.domain.LoadProfile
import vivid.money.elmslie.core.ActorCompat
import javax.inject.Inject

class StreamsActor(
    private val loadProfile: LoadProfile
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {

        is Command.LoadProfileNetwork -> loadProfile.fromNetwork()
            .doOnSuccess { loadProfile.saveToDataBase(it) }
            .mapEvents(
                { profile -> Event.Internal.ProfileLoadedNetwork(profile) },
                { error -> Event.Internal.ErrorLoadingNetwork(error) }
            )

        is Command.CreateStream -> loadProfile.createStream(command.name, command.description)
            .mapEvents(
                { Event.Internal.StreamCreated },
                { error -> Event.Internal.ErrorCreateStream(error) }
            )

        is Command.LoadProfileDB -> loadProfile.fromDataBase()
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