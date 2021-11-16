package ru.s44khin.messenger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

data class ResultStream(
    val streamId: Int,
    val description: String,
    val name: String,
    val topics: List<Topic>
)