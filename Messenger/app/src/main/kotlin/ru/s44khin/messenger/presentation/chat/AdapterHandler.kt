package ru.s44khin.messenger.presentation.chat

interface AdapterHandler {

    fun showProfile(avatar: String, name: String, email: String)

    fun addReaction(messageId: Int, emojiName: String)

    fun removeReaction(messageId: Int, emojiName: String)
}