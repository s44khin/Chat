package ru.s44khin.messenger.data.api

import io.reactivex.Single
import retrofit2.http.*
import ru.s44khin.messenger.data.model.*

interface ApiService {

    @GET("/api/v1/users/me/subscriptions")
    fun getSubsStreams(): Single<BaseSubs>

    @GET("/api/v1/streams")
    fun getAllStreams(): Single<BaseAll>

    @GET("/api/v1/users/me/{stream_id}/topics")
    fun getTopics(@Path("stream_id") streamId: Int): Single<BaseTopics>

    @GET("/api/v1/users")
    fun getMembers(): Single<BaseMembers>

    @GET("/api/v1/users/me")
    fun getSelfProfile(): Single<Profile>

    @GET("/api/v1/messages?&anchor=newest&num_before=150&num_after=0&apply_markdown=false")
    fun getMessages(@Query("narrow") narrow: String): Single<BaseMessages>

    @POST("/api/v1/messages?&type=stream")
    fun sendMessage(
        @Query("to") streamName: String,
        @Query("topic") topicName: String,
        @Query("content") content: String
    ): Single<ResultMessage>

    @POST("/api/v1/messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): Single<ResultReaction>

    @DELETE("/api/v1/messages/{message_id}/reactions")
    fun deleteReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): Single<ResultReaction>
}