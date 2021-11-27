package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.network.ZulipRepository
import javax.inject.Inject

class LoadAllStreams(
    private val repository: ZulipRepository,
    private val dataBase: MessengerDataBase
) {

    fun fromNetwork() = repository.getAllStreams()

    fun fromDataBase() = dataBase.streamsDao().getAll()

    fun saveToDataBase(streams: List<ResultStream>) = dataBase.streamsDao().insertAll(streams)
}