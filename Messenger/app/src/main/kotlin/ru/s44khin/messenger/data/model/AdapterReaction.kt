package ru.s44khin.messenger.data.model

data class AdapterReaction(
    val emojiCode: String,
    val emojiName: String,
    val count: Int,
    val iLiked: Boolean
)