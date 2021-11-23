package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.dao.MessagesDao
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadMessages(
    private val repository: ZulipRepository,
    private val dataBase: MessagesDao
) {

    fun fromNetwork(
        streamId: Int,
        topicName: String
    ) = repository.getMessages(streamId, topicName)

    fun fromDataBase(topicName: String) = dataBase.getAll(topicName)

    fun saveToDataBase(messages: List<Message>) = dataBase.insertAll(messages)

    fun sendMessage(streamName: String, topicName: String, content: String) =
        repository.sendMessage(streamName, topicName, content)

    fun addReaction(messageId: Int, emojiName: String) =
        repository.addReaction(messageId, emojiName)

    fun removeReaction(messageId: Int, emojiName: String) =
        repository.removeReaction(messageId, emojiName)
}