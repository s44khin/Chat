package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadSubsStreams(
    private var repository: ZulipRepository,
    private var dataBase: MessengerDatabase
) {

    fun fromNetwork() = repository.getSubsStreams()

    fun fromDataBase() = dataBase.streamsDao().getAll(true)

    fun unsubscribeFromStream(streamName: String) = repository.unsubscribeFromStream(streamName)

    fun setStreamColor(streamId: Int, color: String) = repository.setStreamColor(streamId, color)

    fun pinStreamToTop(streamId: Int) = repository.pinStreamToTop(streamId)

    fun unpinStreamFromTop(streamId: Int) = repository.unpinStreamFromTop(streamId)

    fun saveToDataBase(streams: List<ResultStream>) = dataBase.streamsDao().insertAll(streams)

    fun createNewStream(streamName: String, description: String) =
        repository.subscribeToStream(streamName, description)
}
