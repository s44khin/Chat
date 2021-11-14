package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class ResultReaction(
    @field:Json(name = "msg") val msg: String,
    @field:Json(name = "result") val result: String
)