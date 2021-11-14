package ru.s44khin.messenger.ui.chat

import ru.s44khin.messenger.data.model.AdapterReaction

sealed class ChatItem {
    data class Message(
        val id: Int,
        val time: String,
        val avatar: String,
        val profile: String,
        val content: String,
        val isMyMessage: Boolean,
        val reactions: MutableList<AdapterReaction> = mutableListOf()
    ) : ChatItem()

    class Date(val date: String) : ChatItem()
}