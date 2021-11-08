package ru.s44khin.messenger.data.model

import com.squareup.moshi.Json

data class Profile(
    @field:Json(name = "full_name") val name: String,
    @field:Json(name = "avatar_url") val avatar: String,
    @field:Json(name = "is_active") val isActive: Boolean,
    @field:Json(name = "email")val email: String
)