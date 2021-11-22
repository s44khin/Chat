package ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm

import ru.s44khin.messenger.data.model.ResultStream

data class State(
    val allStreams: List<ResultStream>? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = false
)

sealed class Event {
    sealed class Ui : Event() {
        object LoadStreamsFirst: Ui()
        object LoadStreamsNetwork: Ui()
        object LoadStreamsDB : Ui()
    }

    sealed class Internal : Event() {
        data class StreamsLoadedNetwork(val streams: List<ResultStream>) : Internal()
        data class StreamsLoadedDB(val streams: List<ResultStream>) : Internal()
        data class ErrorLoadingNetwork(val error: Throwable) : Internal()
        data class ErrorLoadingDB(val error: Throwable?) : Internal()
    }
}

sealed class Effect {
    data class StreamsLoadError(val error: Throwable) : Effect()
}

sealed class Command {
    object LoadStreamsNetwork : Command()
    object LoadStreamsDB : Command()
}