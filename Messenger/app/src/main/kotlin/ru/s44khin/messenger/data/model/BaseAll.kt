package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class BaseAll(
    @Json(name = "streams") val streams: List<Stream>
)