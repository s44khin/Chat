package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.dao.StreamsDao
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadSubsStreams(
    private val repository: ZulipRepository,
    private val dataBase: StreamsDao
) {

    fun fromNetwork() = repository.getSubsStreams()

    fun fromDataBase() = dataBase.getAll()
}