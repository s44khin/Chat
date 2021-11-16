package ru.s44khin.messenger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "profile")
data class Profile(

    @ColumnInfo(name = "user_id")
    @PrimaryKey
    @field:Json(name = "user_id")
    val id: Int,

    @ColumnInfo(name = "full_name")
    @field:Json(name = "full_name")
    val name: String,

    @ColumnInfo(name = "avatar_url")
    @field:Json(name = "avatar_url")
    val avatar: String,

    @ColumnInfo(name = "email")
    @field:Json(name = "email")
    val email: String
)