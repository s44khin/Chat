package ru.s44khin.messenger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SubsStream")
data class SubsStream(

    @ColumnInfo(name = "streamId")
    @PrimaryKey
    val streamId: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "name")
    val name: String,
)