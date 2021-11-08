package ru.s44khin.coursework.ui.chat

import ru.s44khin.coursework.data.model.Message

sealed class ChatItem {
    class MessageItem(val message: Message) : ChatItem()
    class DateItem(val date: Int) : ChatItem()
}