package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class BaseTopics(
    @Json(name = "topic") val topics: List<Topic>
)