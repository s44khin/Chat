package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class Message(
    @field:Json(name = "timestamp") val timestamp: Int,
    @field:Json(name = "avatar_url") val avatar: String,
    @field:Json(name = "sender_full_name") val profile: String,
    @field:Json(name = "content") val content: String,
    @field:Json(name = "sender_id") val senderId: Int,
    @field:Json(name = "reactions") val reactions: List<Reaction>
)