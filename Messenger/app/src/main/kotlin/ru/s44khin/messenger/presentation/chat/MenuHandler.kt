package ru.s44khin.messenger.presentation.chat

interface MenuHandler {

    fun showProfile(avatar: String, name: String, email: String)

    fun addReaction(messageId: Int, emojiName: String)

    fun removeReaction(messageId: Int, emojiName: String)

    fun copyTextToClipboard(content: String)
}