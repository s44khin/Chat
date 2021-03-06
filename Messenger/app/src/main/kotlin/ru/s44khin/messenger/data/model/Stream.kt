package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class Stream(
    @field:Json(name = "description") val description: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "stream_id") val streamId: Int,
    @field:Json(name = "date_created") val date: Int,
    @field:Json(name = "pin_to_top") val pinToTop: Boolean?,
    @field:Json(name = "color") val color: String?,
)