package ru.s44khin.messenger.presentation.chat

import ru.s44khin.messenger.data.model.AdapterReaction

data class ChatItem(
    val id: Int,
    val topicName: String,
    val time: String,
    val avatar: String,
    val profile: String,
    val content: String,
    val isMyMessage: Boolean,
    val reactions: MutableList<AdapterReaction>
)