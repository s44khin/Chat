package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class BaseMembers(
    @field:Json(name = "members") val members: List<Profile>
)