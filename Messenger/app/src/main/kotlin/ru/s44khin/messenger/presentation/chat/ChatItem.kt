package ru.s44khin.messenger.presentation.chat

import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.utils.MY_AVATAR
import ru.s44khin.messenger.utils.MY_ID
import ru.s44khin.messenger.utils.MY_NAME

sealed class ChatItem {

    data class Message(

        val id: Int,
        val topicName: String,
        val time: String,
        val avatar: String,
        val profile: String,
        val content: String,
        val isMyMessage: Boolean,
        val reactions: MutableList<AdapterReaction> = mutableListOf()
    ) : ChatItem()

    class Date(val date: String) : ChatItem()
}