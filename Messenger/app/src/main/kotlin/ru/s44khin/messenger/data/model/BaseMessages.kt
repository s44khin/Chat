package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class BaseMessages(
    @field:Json(name = "anchor") val anchor: String,
    @field:Json(name = "messages") val messages: MutableList<Message>
)