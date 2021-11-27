package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.dataBase.dao.MessagesDao
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.network.ZulipRepository
import javax.inject.Inject

class LoadMessages(
    private val repository: ZulipRepository,
    private val dataBase: MessengerDataBase
) {

    fun fromNetwork(
        streamId: Int,
        topicName: String,
        pageNumber: Int
    ) = repository.getMessages(streamId, topicName, pageNumber)

    fun fromDataBase(topicName: String) = dataBase.messagesDao().getAll(topicName)

    fun saveToDataBase(messages: List<Message>) = dataBase.messagesDao().insertAll(messages)

    fun sendMessage(streamName: String, topicName: String, content: String) =
        repository.sendMessage(streamName, topicName, content)

    fun addReaction(messageId: Int, emojiName: String) =
        repository.addReaction(messageId, emojiName)

    fun removeReaction(messageId: Int, emojiName: String) =
        repository.removeReaction(messageId, emojiName)
}