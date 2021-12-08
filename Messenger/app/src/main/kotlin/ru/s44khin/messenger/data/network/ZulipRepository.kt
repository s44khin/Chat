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

    fun setStreamColor(streamId: Int, color: String): Single<Result> =
        service.updateSettings("[{\"stream_id\":$streamId,\"property\":\"color\",\"value\":\"$color\"}]")

    fun pinStreamToTop(streamId: Int): Single<Result> =
        service.updateSettings("[{\"stream_id\":$streamId,\"property\":\"pin_to_top\",\"value\":true}]")

    fun unpinStreamFromTop(streamId: Int): Single<Result> =
        service.updateSettings("[{\"stream_id\":$streamId,\"property\":\"pin_to_top\",\"value\":false}]")

    fun getMembers(): Single<BaseMembers> = service.getMembers()

    fun getSelfProfile(): Single<Profile> = service.getSelfProfile()

    fun getMessages(
        streamId: Int,
        topicName: String?,
        pageNumber: Int
    ): Single<BaseMessages> {
        val numBefore = pageNumber * 50 + 50
        val numAfter = pageNumber * 50

        return if (topicName == null)
            service.getMessages(
                narrow = "[{\"operator\": \"stream\", \"operand\": $streamId}]",
                numBefore = numBefore,
                numAfter = numAfter
            )
        else
            service.getMessages(
                narrow = "[{\"operator\": \"stream\", \"operand\": $streamId}, " +
                        "{\"operator\": \"topic\", \"operand\": \"$topicName\"}]",
                numBefore = numBefore,
                numAfter = numAfter
            )
    }

    fun sendMessage(
        streamName: String,
        topicName: String?,
        content: String
    ): Single<ResultMessage> = service.sendMessage(streamName, topicName ?: "", content)

    fun addReaction(messageId: Int, emojiName: String) = service.addReaction(messageId, emojiName)

    fun removeReaction(
        messageId: Int,
        emojiName: String
    ) = service.deleteReaction(messageId, emojiName)
}