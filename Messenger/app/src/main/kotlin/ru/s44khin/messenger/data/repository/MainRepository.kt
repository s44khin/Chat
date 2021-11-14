package ru.s44khin.messenger.data.repository

import io.reactivex.Single
import ru.s44khin.messenger.data.api.RetrofitClient
import ru.s44khin.messenger.data.model.*

class MainRepository {

    fun getSubsStreams(): Single<BaseSubs> = RetrofitClient.apiService.getSubsStreams()

    fun getAllStreams(): Single<BaseAll> = RetrofitClient.apiService.getAllStreams()

    fun getTopics(streamId: Int): Single<BaseTopics> = RetrofitClient.apiService.getTopics(streamId)

    fun getMembers(): Single<BaseMembers> = RetrofitClient.apiService.getMembers()

    fun getSelfProfile(): Single<Profile> = RetrofitClient.apiService.getSelfProfile()

    fun getMessages(
        streamId: Int,
        topicName: String
    ): Single<BaseMessages> = RetrofitClient.apiService.getMessages(
        "[{\"operator\": \"stream\", \"operand\": $streamId}, " +
                "{\"operator\": \"topic\", \"operand\": \"$topicName\"}]"
    )

    fun sendMessage(
        streamName: String,
        topicName: String,
        content: String
    ): Single<ResultMessage> = RetrofitClient.apiService.sendMessage(streamName, topicName, content)

    fun addReaction(
        messageId: Int,
        emojiName: String
    ) = RetrofitClient.apiService.addReaction(messageId, emojiName)

    fun deleteReaction(
        messageId: Int,
        emojiName: String
    ) = RetrofitClient.apiService.deleteReaction(messageId, emojiName)
}