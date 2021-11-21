package ru.s44khin.messenger.presentation.members.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class MembersReducer: DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when(event) {
        is Event.Internal.ErrorLoading -> {
            state { copy(error = event.error, isLoading = false) }
            effects { Effect.MembersLoadError(event.error) }
        }
        is Event.Internal.MembersLoaded -> {
            state { copy(members = event.members, isLoading = false, error = null) }
        }
        is Event.Ui.LoadMembers -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.LoadMembers }
        }
    }
}