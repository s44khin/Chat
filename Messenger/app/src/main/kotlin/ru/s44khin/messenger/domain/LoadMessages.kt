package ru.s44khin.messenger.domain

import okhttp3.MultipartBody
import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadMessages(
    private val repository: ZulipRepository,
    private val dataBase: MessengerDatabase
) {

    fun fromNetwork(
        streamId: Int,
        topicName: String?,
        pageNumber: Int
    ) = repository.getMessages(streamId, topicName, pageNumber)

    fun fromDataBase(topicName: String) = dataBase.messagesDao().getAll(topicName)

    fun saveToDataBase(messages: List<Message>) = dataBase.messagesDao().insertAll(messages)

    fun sendMessage(streamName: String, topicName: String?, content: String) =
        repository.sendMessage(streamName, topicName, content)

    fun editMessage(id: Int, content: String) = repository.editMessage(id, content)

    fun editMessageTopic(id: Int, topicName: String) = repository.editMessageTopic(id, topicName)

    fun addReaction(messageId: Int, emojiName: String) =
        repository.addReaction(messageId, emojiName)

    fun removeReaction(messageId: Int, emojiName: String) =
        repository.removeReaction(messageId, emojiName)

    fun deleteMessage(id: Int) = repository.deleteMessage(id)

    fun sendPicture(filePart: MultipartBody.Part) = repository.sendPicture(filePart)
}
