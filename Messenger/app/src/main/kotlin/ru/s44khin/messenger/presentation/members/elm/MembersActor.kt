package ru.s44khin.messenger.presentation.members.elm

import io.reactivex.Observable
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.domain.LoadMembers
import vivid.money.elmslie.core.ActorCompat
import javax.inject.Inject

class MembersActor(
    private val loadMembers: LoadMembers
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {

        is Command.LoadMembersNetwork -> loadMembers.fromNetwork()
            .doOnSuccess { loadMembers.saveToDataBase(it.members) }
            .mapEvents(
                { members -> Event.Internal.MembersLoadedNetwork(members.members) },
                { error -> Event.Internal.ErrorLoadingNetwork(error) }
            )

        is Command.LoadMembersDB -> loadMembers.fromDataBase()
            .mapEvents(
                { members ->
                    if (members.isEmpty())
                        Event.Internal.ErrorLoadingDataBase(null)
                    else
                        Event.Internal.MembersLoadedDB(members)
                },
                { error -> Event.Internal.ErrorLoadingDataBase(error) }
            )
    }
}