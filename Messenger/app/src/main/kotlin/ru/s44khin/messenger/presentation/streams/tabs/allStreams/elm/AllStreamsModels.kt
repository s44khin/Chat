package ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm

import ru.s44khin.messenger.data.model.ResultStream

data class State(
    val allStreams: List<ResultStream> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false
)

sealed class Event {
    sealed class Ui : Event() {
        object LoadStreams: Ui()
    }

    sealed class Internal : Event() {
        data class StreamsLoaded(val streams: List<ResultStream>) : Internal()
        data class ErrorLoading(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    data class StreamsLoadError(val error: Throwable) : Effect()
}

sealed class Command {
    object LoadStreams : Command()
}