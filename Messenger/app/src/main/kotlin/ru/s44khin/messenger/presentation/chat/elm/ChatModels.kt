package ru.s44khin.messenger.presentation.chat.elm

import ru.s44khin.messenger.presentation.chat.ChatItem

data class State(
    val messages: List<ChatItem>? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = false,
    val pageNumber: Int = INITIAL_PAGE
) {

    companion object {
        const val INITIAL_PAGE = 0
    }
}

sealed class Event {

    sealed class Ui : Event() {

        object LoadNextPage : Ui()

        object LoadMessagesDB : Ui()

        data class DeleteMessage(val id: Int) : Ui()

        data class SendMessage(val content: String) : Ui()

        data class EditMessage(
            val id: Int,
            val content: String
        ) : Ui()

        data class AddReaction(
            val messageId: Int,
            val emojiName: String
        ) : Ui()

        data class RemoveReaction(
            val messageId: Int,
            val emojiName: String
        ) : Ui()
    }

    sealed class Internal : Event() {

        data class MessagesLoadedNetwork(val messages: List<ChatItem>) : Internal()

        data class MessagesLoadedDB(val messages: List<ChatItem>) : Internal()

        object MessageDeleted : Internal()

        data class MessageSent(
            val content: String,
            val topicName: String
        ) : Internal()

        object ReactionAdded : Internal()

        object ReactionRemoved : Internal()

        object MessageEdited : Internal()

        data class ReactionAddError(val error: Throwable) : Internal()

        data class ReactionRemoveError(val error: Throwable) : Internal()

        data class ErrorLoadingNetwork(val error: Throwable) : Internal()

        data class ErrorLoadingDB(val error: Throwable) : Internal()

        data class ErrorSendMessage(val error: Throwable) : Internal()

        data class ErrorDeleteMessage(val error: Throwable) : Internal()

        data class EditMessageError(val error: Throwable) : Internal()
    }
}

sealed class Effect {

    data class MessagesLoadError(val error: Throwable) : Effect()
}

sealed class Command {

    data class LoadPage(val pageNumber: Int) : Command()

    object LoadMessagesDB : Command()

    data class SendMessage(val content: String) : Command()

    data class EditMessage(val id: Int, val content: String) : Command()

    data class DeleteMessage(val id: Int) : Command()

    data class AddReaction(
        val messageId: Int,
        val emojiName: String
    ) : Command()

    data class RemoveReaction(
        val messageId: Int,
        val emojiName: String
    ) : Command()
}