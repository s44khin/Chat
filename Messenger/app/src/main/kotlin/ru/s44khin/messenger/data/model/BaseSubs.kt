package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class BaseSubs(
    @Json(name = "subscriptions") val subscriptions: List<Stream>
)