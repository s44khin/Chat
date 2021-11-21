package ru.s44khin.messenger.presentation.members.elm

import io.reactivex.Observable
import ru.s44khin.messenger.domain.LoadMembers
import vivid.money.elmslie.core.ActorCompat

class MembersActor(
    private val loadMembers: LoadMembers
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadMembers -> loadMembers.execute()
            .mapEvents(
                { members -> Event.Internal.MembersLoaded(members.members) },
                { error -> Event.Internal.ErrorLoading(error) }
            )
    }
}