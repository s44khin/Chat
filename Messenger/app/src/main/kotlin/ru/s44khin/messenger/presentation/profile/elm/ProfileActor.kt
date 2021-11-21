package ru.s44khin.messenger.presentation.profile.elm

import io.reactivex.Observable
import ru.s44khin.messenger.domain.LoadProfile
import vivid.money.elmslie.core.ActorCompat

class ProfileActor(
    private val loadProfile: LoadProfile
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadProfile -> loadProfile.execute()
            .mapEvents(
                { profile -> Event.Internal.ProfileLoaded(profile) },
                { error -> Event.Internal.ErrorLoading(error) }
            )
    }
}