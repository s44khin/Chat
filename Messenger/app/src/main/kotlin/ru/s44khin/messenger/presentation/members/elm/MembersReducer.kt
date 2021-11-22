package ru.s44khin.messenger.presentation.members.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class MembersReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {
        is Event.Internal.ErrorLoadingNetwork -> {
            state { copy(error = event.error, isLoadingNetwork = false) }
            effects { Effect.MembersLoadError(event.error) }
        }
        is Event.Internal.ErrorLoadingDataBase -> {
            state { copy() }
        }
        is Event.Internal.MembersLoadedNetwork -> {
            state {
                copy(
                    members = event.members,
                    isLoadingNetwork = false,
                    isLoadingDB = false,
                    error = null
                )
            }
        }
        is Event.Internal.MembersLoadedDB -> {
            state { copy(members = event.members, isLoadingDB = false, error = null) }
        }
        is Event.Ui.LoadMembersFirst -> if (state.members != null) {
            state { copy(members = state.members, isLoadingNetwork = false, isLoadingDB = false) }
        } else {
            commands { +Command.LoadMembersDB }
        }
        is Event.Ui.LoadMembersNetwork -> {
            state { copy(isLoadingNetwork = true, error = null) }
            commands { +Command.LoadMembersNetwork }
        }
        is Event.Ui.LoadMembersDB -> {
            state { copy(isLoadingDB = true, error = null) }
            commands { +Command.LoadMembersDB }
        }
    }
}