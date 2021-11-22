package ru.s44khin.messenger.presentation.streams.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class StreamsReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {
        is Event.Internal.Error -> {
            state { copy() }
            effects { Effect.ProfileLoadError(event.error) }
        }
        is Event.Internal.ProfileLoadedNetwork -> {
            state { copy(profile = state.profile) }
        }
        is Event.Internal.ProfileLoadedDB -> {
            state { copy(profile = state.profile) }
        }
        is Event.Ui.LoadProfileNetwork -> {
            commands { +Command.LoadProfileNetwork }
        }
        is Event.Ui.LoadProfileDB -> {
            commands { +Command.LoadProfileDB }
        }
    }
}