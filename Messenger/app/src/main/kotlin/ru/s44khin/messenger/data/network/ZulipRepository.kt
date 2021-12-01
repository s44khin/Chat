package ru.s44khin.messenger.data.network

import io.reactivex.Single
import ru.s44khin.messenger.data.model.*

class ZulipRepository(
    private val service: ZulipService
) {

    fun getSubsStreams(): Single<BaseSubs> = service.getSubsStreams()

    fun getAllStreams(): Single<BaseAll> = service.getAllStreams()

    fun getTopics(streamId: Int): Single<BaseTopics> = service.getTopics(streamId)

    fun subscribeToStream(streamName: String, description: String): Single<Result> =
        service.subscribeToStream("[{\"name\":\"${streamName}\", \"description\":\"${description}\"}]")

    fun unsubscribeFromStream(streamName: String): Single<Result> =
        service.unsubscribeFromStream("[\"${streamName}\"]")

    fun getMembers(): Single<BaseMembers> = service.getMembers()

    fun getSelfProfile(): Single<Profile> = service.getSelfProfile()

    fun getMessages(
        streamId: Int,
        topicName: String,
        pageNumber: Int
    ): Single<BaseMessages> {
        val numBefore = pageNumber * 50 + 50
        val numAfter = pageNumber * 50

        return service.getMessages(
            narrow = "[{\"operator\": \"stream\", \"operand\": $streamId}, " +
                    "{\"operator\": \"topic\", \"operand\": \"$topicName\"}]",
            numBefore = numBefore,
            numAfter = numAfter
        )
    }

    fun sendMessage(
        streamName: String,
        topicName: String,
        content: String
    ): Single<ResultMessage> = service.sendMessage(streamName, topicName, content)

    fun addReaction(messageId: Int, emojiName: String) = service.addReaction(messageId, emojiName)

    fun removeReaction(
        messageId: Int,
        emojiName: String
    ) = service.deleteReaction(messageId, emojiName)
}