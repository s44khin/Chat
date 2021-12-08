package ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.elm

import ru.s44khin.messenger.data.model.ResultStream

data class State(
    val allStreams: List<ResultStream>? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = false
)

sealed class Event {

    sealed class Ui : Event() {

        object LoadStreamsFirst : Ui()

        object LoadStreamsNetwork : Ui()

        data class SubscribeToStream(
            val streamName: String,
            val description: String
        ) : Ui()
    }

    sealed class Internal : Event() {

        data class StreamsLoadedNetwork(val streams: List<ResultStream>) : Internal()

        data class StreamsLoadedDB(val streams: List<ResultStream>) : Internal()

        object SuccessfulSubscriptionToStream : Internal()

        data class ErrorLoadingNetwork(val error: Throwable) : Internal()

        data class ErrorLoadingDB(val error: Throwable?) : Internal()

        object ErrorSubscribeToStream : Internal()
    }
}

sealed class Effect {

    data class StreamsLoadError(val error: Throwable) : Effect()

    object SubscribeToStreamError : Effect()
}

sealed class Command {

    object LoadStreamsNetwork : Command()

    object LoadStreamsDB : Command()

    data class SubscribeToStream(
        val streamName: String,
        val description: String
    ) : Command()
}