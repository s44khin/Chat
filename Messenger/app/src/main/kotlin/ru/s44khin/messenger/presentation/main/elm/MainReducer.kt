package ru.s44khin.messenger.presentation.main.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class MainReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {

        is Event.Ui.LoadProfile -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.LoadProfileDatabase }
        }

        is Event.Internal.ErrorLoadingDatabase -> {
            commands { +Command.LoadProfileNetwork }
        }

        is Event.Internal.ErrorLoadingNetwork -> {
            state { copy(isLoading = false, error = event.error) }
        }

        is Event.Internal.ProfileLoadedDatabase -> {
            state { copy(profile = event.profile, isLoading = false, error = null) }
            commands { +Command.LoadProfileNetwork }
        }

        is Event.Internal.ProfileLoadedNetwork -> {
            state { copy(profile = event.profile, isLoading = false, error = null) }
        }
    }
}