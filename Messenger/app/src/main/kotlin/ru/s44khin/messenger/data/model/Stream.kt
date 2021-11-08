package ru.s44khin.messenger.data.model

import android.graphics.Color
import com.squareup.moshi.Json

data class Stream(
    @field:Json(name = "description") val description: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "stream_id") val streamId: Int,
)