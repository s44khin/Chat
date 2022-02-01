package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class ResultImage(
    @field:Json(name = "result") val result: String,
    @field:Json(name = "uri") val uri: String
)