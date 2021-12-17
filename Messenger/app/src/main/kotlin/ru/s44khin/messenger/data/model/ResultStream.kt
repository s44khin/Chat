package ru.s44khin.messenger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.s44khin.messenger.data.dataBase.converters.TopicConverter

@Entity(tableName = "stream")
data class ResultStream(
    @ColumnInfo(name = "streamId")
    @PrimaryKey
    val streamId: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "date")
    val date: Int,

    @ColumnInfo(name = "pin_to_top")
    val pinToTop: Boolean?,

    @ColumnInfo(name = "color")
    val color: String?,

    @ColumnInfo(name = "subscription")
    val subscription: Boolean,

    @ColumnInfo(name = "topics")
    @TypeConverters(TopicConverter::class)
    val topics: List<Topic>
)