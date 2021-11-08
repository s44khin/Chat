package ru.s44khin.messenger.data.model

data class AdapterMessage(
    val time: String,
    val avatar: String,
    val profile: String,
    val content: String,
    val isMyMessage: Boolean,
    val reactions: MutableList<AdapterReaction>
)