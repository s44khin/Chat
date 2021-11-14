package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class Topic(
    @Json(name = "max_id") val maxId: Int,
    @Json(name = "name") val name: String
)