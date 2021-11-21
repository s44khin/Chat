package ru.s44khin.messenger.ui.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.s44khin.messenger.data.dataBase.converters.ReactionConverter
import ru.s44khin.messenger.data.model.AdapterReaction

sealed class ChatItem {

    @Entity(tableName = "message")
    data class Message(

        @ColumnInfo(name = "id")
        @PrimaryKey
        val id: Int,

        @ColumnInfo(name = "topicName")
        val topicName: String,

        @ColumnInfo(name = "time")
        val time: String,

        @ColumnInfo(name = "avatar")
        val avatar: String,

        @ColumnInfo(name = "profile")
        val profile: String,

        @ColumnInfo(name = "content")
        val content: String,

        @ColumnInfo(name = "isMyMessage")
        val isMyMessage: Boolean,

        @ColumnInfo(name = "reaction")
        @TypeConverters(ReactionConverter::class)
        val reactions: MutableList<AdapterReaction>
    ) : ChatItem()

    class Date(val date: String) : ChatItem()
}