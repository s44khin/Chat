package ru.s44khin.messenger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import ru.s44khin.messenger.data.dataBase.converters.ReactionConverter

@Entity(tableName = "message")
data class Message(

    @ColumnInfo(name = "id")
    @PrimaryKey
    @field:Json(name = "id")
    val id: Int,

    @ColumnInfo(name = "stream_id")
    @field:Json(name = "stream_id")
    val streamId: Int,

    @ColumnInfo(name = "topicName")
    @field:Json(name = "subject")
    val topicName: String,

    @ColumnInfo(name = "timestamp")
    @field:Json(name = "timestamp")
    val timestamp: Int,

    @ColumnInfo(name = "avatar_url")
    @field:Json(name = "avatar_url")
    val avatar: String,

    @ColumnInfo(name = "sender_full_name")
    @field:Json(name = "sender_full_name")
    val profile: String,

    @ColumnInfo(name = "content")
    @field:Json(name = "content")
    val content: String,

    @ColumnInfo(name = "sender_id")
    @field:Json(name = "sender_id")
    val senderId: Int,

    @ColumnInfo(name = "reaction")
    @TypeConverters(ReactionConverter::class)
    @field:Json(name = "reactions")
    val reactions: List<Reaction>
)