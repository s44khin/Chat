package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.dataBase.dao.StreamsDao
import ru.s44khin.messenger.data.network.ZulipRepository
import javax.inject.Inject

class LoadSubsStreams(
    private var repository: ZulipRepository,
    private var dataBase: MessengerDataBase
) {

    fun fromNetwork() = repository.getSubsStreams()

    fun fromDataBase() = dataBase.streamsDao().getAll()
}