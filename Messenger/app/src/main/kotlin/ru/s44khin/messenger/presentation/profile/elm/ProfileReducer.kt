package ru.s44khin.messenger.presentation.profile.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {
        is Event.Internal.ErrorLoading -> {
            state { copy(error = event.error, isLoading = false) }
            effects { Effect.ProfileLoadError(event.error) }
        }
        is Event.Internal.ProfileLoaded -> {
            state { copy(profile = event.profile, isLoading = false, error = null) }
        }
        is Event.Ui.LoadProfile -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.LoadProfile }
        }
    }
}