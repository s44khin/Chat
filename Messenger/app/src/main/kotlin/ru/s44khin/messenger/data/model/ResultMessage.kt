package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class ResultMessage(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "result") val result: String
)