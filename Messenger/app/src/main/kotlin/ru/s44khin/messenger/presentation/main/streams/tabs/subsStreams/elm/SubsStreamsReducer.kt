package ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class SubsStreamsReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {

        is Event.Internal.ErrorLoadingNetwork -> {
            state { copy(error = event.error, isLoadingNetwork = false, isLoadingDB = false) }
            effects { Effect.StreamsLoadError(event.error) }
        }

        is Event.Internal.ErrorLoadingDB -> {
            state { copy(isLoadingDB = true, isLoadingNetwork = true, error = null) }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Internal.ErrorUnsubscribeFromStream -> {
            effects { Effect.ErrorUnsubscribeFromStream }
        }

        is Event.Internal.ErrorSetStreamColor -> {
            effects { Effect.ErrorSetStreamColor }
        }

        is Event.Internal.ErrorPinned -> {
            effects { Effect.ErrorPinned }
        }

        is Event.Internal.ErrorUnpinned -> {
            effects { Effect.ErrorUnpinned }
        }

        is Event.Internal.ErrorCreateStream -> {
            effects { Effect.ErrorCreateStream }
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
            state {
                copy(
                    subsStreams = event.streams,
                    isLoadingDB = false,
                    isLoadingNetwork = true,
                    error = null
                )
            }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Internal.SuccessfulUnsubscribeFromStream -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Internal.SuccessfulSetStreamColor -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Internal.StreamCreated -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Internal.SuccessfulPinned -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Internal.SuccessfulUnpinned -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Ui.LoadStreamsFirst -> if (state.subsStreams != null) {
            state {
                copy(
                    subsStreams = state.subsStreams,
                    isLoadingNetwork = false,
                    isLoadingDB = false
                )
            }
        } else {
            state { copy(isLoadingNetwork = true, isLoadingDB = true) }
            commands { +Command.LoadStreamsDB }
        }

        is Event.Ui.LoadStreamsNetwork -> {
            state { copy(isLoadingNetwork = true, error = null) }
            commands { +Command.LoadStreamsNetwork }
        }

        is Event.Ui.UnsubscribeFromStream -> {
            commands { +Command.UnsubscribeFromStream(event.streamName) }
        }

        is Event.Ui.SetStreamColor -> {
            commands { +Command.SetStreamColor(event.streamId, event.color) }
        }

        is Event.Ui.PinStreamToTop -> {
            commands { +Command.PinStreamToTop(event.streamId) }
        }

        is Event.Ui.UnpinStreamFromTop -> {
            commands { +Command.UnpinFromTop(event.streamId) }
        }

        is Event.Ui.CreateNewStream -> {
            commands { +Command.CreateNewStream(event.streamName, event.description) }
        }
    }
}