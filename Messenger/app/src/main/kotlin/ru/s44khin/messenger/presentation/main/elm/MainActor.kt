package ru.s44khin.messenger.presentation.main.elm

import io.reactivex.Observable
import ru.s44khin.messenger.domain.LoadMembers
import vivid.money.elmslie.core.ActorCompat

class MainActor(
    private val loadMembers: LoadMembers
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {

        is Command.LoadProfileDatabase -> loadMembers.getSelfProfileFromDatabase()
            .mapEvents(
                { profile -> Event.Internal.ProfileLoadedDatabase(profile) },
                { error -> Event.Internal.ErrorLoadingDatabase(error) }
            )

        is Command.LoadProfileNetwork -> loadMembers.getSelfProfileFromNetwork()
            .mapEvents(
                { profile -> Event.Internal.ProfileLoadedNetwork(profile) },
                { error -> Event.Internal.ErrorLoadingNetwork(error) }
            )
    }
}