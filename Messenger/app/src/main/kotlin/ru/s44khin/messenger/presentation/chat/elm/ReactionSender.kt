package ru.s44khin.messenger.presentation.chat.elm

interface ReactionSender {

    fun addReaction(messageId: Int, emojiName: String)

    fun removeReaction(messageId: Int, emojiName: String)
}