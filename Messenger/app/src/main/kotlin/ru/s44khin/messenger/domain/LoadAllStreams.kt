package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.dao.StreamsDao
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadAllStreams(
    private val repository: ZulipRepository,
    private val dataBase: StreamsDao
) {

    fun fromNetwork() = repository.getAllStreams()

    fun fromDataBase() = dataBase.getAll()

    fun saveToDataBase(streams: List<ResultStream>) = dataBase.insertAll(streams)
}