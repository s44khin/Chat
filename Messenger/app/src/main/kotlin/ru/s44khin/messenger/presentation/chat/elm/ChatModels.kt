package ru.s44khin.messenger.presentation.chat.elm

import ru.s44khin.messenger.presentation.chat.ChatItem

data class State(
    val messages: List<ChatItem>? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = false
)

sealed class Event {
    sealed class Ui : Event() {
        object LoadMessagesNetwork : Ui()
        object LoadMessagesDB : Ui()
        data class SendMessage(val content: String) : Ui()
        data class AddReaction(val messageId: Int, val emojiName: String) : Ui()
        data class RemoveReaction(val messageId: Int, val emojiName: String) : Ui()
    }

    sealed class Internal : Event() {
        data class MessagesLoadedNetwork(val messages: List<ChatItem>) : Internal()
        data class MessagesLoadedDB(val messages: List<ChatItem>) : Internal()
        data class MessageSent(val content: String, val topicName: String) : Internal()
        object ReactionAdded : Internal()
        object ReactionRemoved : Internal()
        data class ReactionAddError(val error: Throwable) : Internal()
        data class ReactionRemoveError(val error: Throwable) : Internal()
        data class ErrorLoadingNetwork(val error: Throwable) : Internal()
        data class ErrorLoadingDB(val error: Throwable) : Internal()
        data class ErrorSendMessage(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    data class MessagesLoadError(val error: Throwable) : Effect()
}

sealed class Command {
    object LoadMessagesNetwork : Command()
    object LoadMessagesDB : Command()
    data class SendMessage(val content: String) : Command()
    data class AddReaction(val messageId: Int, val emojiName: String) : Command()
    data class RemoveReaction(val messageId: Int, val emojiName: String) : Command()
}