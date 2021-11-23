package ru.s44khin.messenger.presentation.chat

import ru.s44khin.messenger.data.model.AdapterReaction

sealed class ChatItem {

    data class Message(

        val id: Int,
        val topicName: String,
        val time: String,
        val avatar: String,
        val profile: String,
        val content: String,
        val isMyMessage: Boolean,
        val reactions: MutableList<AdapterReaction>
    ) : ChatItem()

    class Date(val date: String) : ChatItem()
}