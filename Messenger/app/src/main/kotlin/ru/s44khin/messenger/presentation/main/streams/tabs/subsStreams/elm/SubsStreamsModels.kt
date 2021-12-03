package ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm

import ru.s44khin.messenger.data.model.ResultStream

data class State(
    val subsStreams: List<ResultStream>? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = false
)

sealed class Event {

    sealed class Ui : Event() {

        object LoadStreamsFirst : Ui()

        object LoadStreamsNetwork : Ui()

        data class UnsubscribeFromStream(val streamName: String) : Ui()

        data class SetStreamColor(
            val streamId: Int,
            val color: String
        ) : Ui()

        data class PinStreamToTop(val streamId: Int) : Ui()

        data class UnpinStreamFromTop(val streamId: Int) : Ui()
    }

    sealed class Internal : Event() {

        data class StreamsLoadedNetwork(val streams: List<ResultStream>) : Internal()

        data class StreamsLoadedDB(val streams: List<ResultStream>) : Internal()

        object SuccessfulUnsubscribeFromStream : Internal()

        object SuccessfulSetStreamColor : Internal()

        object SuccessfulPinned : Internal()

        object SuccessfulUnpinned : Internal()

        data class ErrorLoadingNetwork(val error: Throwable) : Internal()

        data class ErrorLoadingDB(val error: Throwable?) : Internal()

        object ErrorUnsubscribeFromStream : Internal()

        object ErrorSetStreamColor : Internal()

        object ErrorPinned : Internal()

        object ErrorUnpinned : Internal()
    }
}

sealed class Effect {

    data class StreamsLoadError(val error: Throwable) : Effect()

    object ErrorUnsubscribeFromStream : Effect()

    object ErrorSetStreamColor : Effect()

    object ErrorPinned : Effect()

    object ErrorUnpinned : Effect()
}

sealed class Command {

    object LoadStreamsNetwork : Command()

    object LoadStreamsDB : Command()

    data class UnsubscribeFromStream(val streamName: String) : Command()

    data class SetStreamColor(
        val streamId: Int,
        val color: String
    ) : Command()

    data class PinStreamToTop(val streamId: Int) : Command()

    data class UnpinFromTop(val streamId: Int) : Command()
}