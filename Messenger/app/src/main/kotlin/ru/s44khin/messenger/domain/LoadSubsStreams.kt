package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadSubsStreams(
    private var repository: ZulipRepository,
    private var dataBase: MessengerDatabase
) {

    fun fromNetwork() = repository.getSubsStreams()

    fun fromDataBase() = dataBase.streamsDao().getAll()

    fun unsubscribeFromStream(streamName: String) = repository.unsubscribeFromStream(streamName)
}
