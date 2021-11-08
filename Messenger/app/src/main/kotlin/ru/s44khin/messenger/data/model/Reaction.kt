package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class Reaction(
    @field:Json(name = "emoji_name") val emojiName: String,
    @field:Json(name = "emoji_code") val emojiCode: String,
    @field:Json(name = "user") val user: User
) {
    data class User(
        @field:Json(name = "id") val id: Int
    )
}