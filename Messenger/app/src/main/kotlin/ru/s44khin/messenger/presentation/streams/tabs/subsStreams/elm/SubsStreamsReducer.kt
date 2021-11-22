package ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class SubsStreamsReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {
        is Event.Internal.ErrorLoadingNetwork -> {
            state { copy(error = event.error, isLoadingNetwork = false) }
            effects { Effect.StreamsLoadError(event.error) }
        }
        is Event.Internal.ErrorLoadingDB -> {
            state { copy() }
        }
        is Event.Internal.StreamsLoadedNetwork -> {
            state {
                copy(
                    subsStreams = event.streams,
                    isLoadingNetwork = false,
                    isLoadingDB = false,
                    error = null
                )
            }
        }
        is Event.Internal.StreamsLoadedDB -> {
            state { copy(subsStreams = event.streams, isLoadingDB = false, error = null) }
        }
        is Event.Ui.LoadStreamsNetwork -> {
            state { copy(isLoadingNetwork = true, error = null) }
            commands { +Command.LoadStreamsNetwork }
        }
        is Event.Ui.LoadStreamsDB -> {
            state { copy(isLoadingDB = true, error = null) }
            commands { +Command.LoadStreamsDB }
        }
    }
}