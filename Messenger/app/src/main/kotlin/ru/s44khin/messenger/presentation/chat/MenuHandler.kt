package ru.s44khin.messenger.presentation.chat

interface MenuHandler {

    fun showProfile(avatar: String, name: String, email: String)

    fun addReaction(messageId: Int, emojiName: String)

    fun removeReaction(messageId: Int, emojiName: String)

    fun copyTextToClipboard(content: String)

    fun deleteMessage(id: Int)

    fun editMessage(message: ChatItem)

    fun sendMessageToTopic(content: String, topic: String)

    fun editTopic(id: Int, topic: String)

    fun showEditTopicMenu(id: Int)
}