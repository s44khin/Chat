package ru.s44khin.messenger.ui.chat

import ru.s44khin.messenger.data.model.AdapterMessage
import ru.s44khin.messenger.data.model.Message

sealed class ChatItem {
    class MessageItem(val message: AdapterMessage) : ChatItem()
    class DateItem(val date: String) : ChatItem()
}