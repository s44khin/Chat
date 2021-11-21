package ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class SubsStreamsReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {
        is Event.Internal.ErrorLoading -> {
            state { copy(error = event.error, isLoading = false) }
            effects { Effect.StreamsLoadError(event.error) }
        }
        is Event.Internal.StreamsLoaded -> {
            state { copy(subsStreams = event.streams, isLoading = false, error = null) }
        }
        is Event.Ui.LoadStreams -> {
            state { copy(isLoading = true, error = null) }
            commands { +Command.LoadStreams }
        }
    }
}