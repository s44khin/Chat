package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadAllStreams(
    private val repository: ZulipRepository,
    private val dataBase: MessengerDatabase
) {

    fun fromNetwork() = repository.getAllStreams()

    fun fromDataBase() = dataBase.streamsDao().getAll()

    fun saveToDataBase(streams: List<ResultStream>) = dataBase.streamsDao().insertAll(streams)

    fun subscribeToStream(streamName: String, description: String) =
        repository.subscribeToStream(streamName, description)
}
