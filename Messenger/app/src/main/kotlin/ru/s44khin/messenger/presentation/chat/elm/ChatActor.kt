package ru.s44khin.messenger.presentation.chat.elm

import io.reactivex.Observable
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.model.Reaction
import ru.s44khin.messenger.domain.LoadMessages
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.utils.MY_ID
import ru.s44khin.messenger.utils.parse
import vivid.money.elmslie.core.ActorCompat

class ChatActor(
    private val loadMessages: LoadMessages,
    private val streamId: Int,
    private val streamName: String,
    private val topicName: String?
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {

        is Command.LoadPage -> loadMessages.fromNetwork(streamId, topicName, command.pageNumber)
            .doOnSuccess { loadMessages.saveToDataBase(it.messages) }
            .mapEvents(
                { baseMessages ->
                    Event.Internal.MessagesLoadedNetwork(baseMessages.messages.toListOfChatItems())
                },
                { error -> Event.Internal.ErrorLoadingNetwork(error) }
            )

        is Command.LoadMessagesDB -> loadMessages.fromDataBase(topicName ?: "")
            .mapEvents(
                { messages -> Event.Internal.MessagesLoadedDB(messages.toListOfChatItems()) },
                { error -> Event.Internal.ErrorLoadingDB(error) }
            )

        is Command.SendMessage -> loadMessages.sendMessage(streamName, topicName, command.content)
            .mapEvents(
                { Event.Internal.MessageSent(command.content, topicName ?: "(no topic)") },
                { error -> Event.Internal.ErrorSendMessage(error) }
            )

        is Command.SendMessageToTopic -> loadMessages.sendMessage(
            streamName,
            command.topicName,
            command.content
        )
            .mapEvents(
                { Event.Internal.MessageSent(command.content, command.topicName) },
                { error -> Event.Internal.ErrorSendMessage(error) }
            )

        is Command.EditMessageTopic -> loadMessages.editMessageTopic(command.id, command.topicName)
            .mapEvents(
                { Event.Internal.MessageTopicChanged },
                { error -> Event.Internal.ErrorSendMessage(error) }
            )

        is Command.AddReaction -> loadMessages.addReaction(command.messageId, command.emojiName)
            .mapEvents(
                { Event.Internal.ReactionAdded },
                { error -> Event.Internal.ReactionAddError(error) }
            )

        is Command.RemoveReaction -> loadMessages.removeReaction(
            command.messageId,
            command.emojiName
        )
            .mapEvents(
                { Event.Internal.ReactionRemoved },
                { error -> Event.Internal.ReactionRemoveError(error) }
            )

        is Command.DeleteMessage -> loadMessages.deleteMessage(command.id)
            .mapEvents(
                { Event.Internal.MessageDeleted },
                { error -> Event.Internal.ErrorDeleteMessage(error) }
            )

        is Command.EditMessage -> loadMessages.editMessage(command.id, command.content)
            .mapEvents(
                { Event.Internal.MessageEdited },
                { error -> Event.Internal.EditMessageError(error) }
            )
    }

    private fun List<Reaction>.toAdapterReactions(): MutableList<AdapterReaction> {
        val result = mutableListOf<AdapterReaction>()
        val map = mutableMapOf<Pair<String, String>, Int>()
        val reactionsILiked = mutableListOf<String>()

        for (reaction in this) {
            if (reaction.emojiCode to reaction.emojiName in map.keys)
                map[reaction.emojiCode to reaction.emojiName] =
                    map[reaction.emojiCode to reaction.emojiName]!! + 1
            else
                map[reaction.emojiCode to reaction.emojiName] = 1

            if (reaction.user.id == MY_ID)
                reactionsILiked.add(reaction.emojiCode)
        }

        for ((emoji, count) in map)
            result.add(
                AdapterReaction(
                    emojiCode = emoji.first,
                    emojiName = emoji.second,
                    count = count,
                    iLiked = emoji.first in reactionsILiked
                )
            )

        return result
    }

    private fun Message.toChatItem(avatarIsNull: Boolean = false) = ChatItem(
        id = this.id,
        topicName = this.topicName,
        time = parse(this.timestamp),
        avatar = if (avatarIsNull) null else this.avatar,
        email = this.email,
        profile = this.profile,
        content = this.content,
        reactions = this.reactions.toAdapterReactions(),
        isMyMessage = MY_ID == this.senderId
    )

    private fun List<Message>.toListOfChatItems(): List<ChatItem> {
        val result = mutableListOf<ChatItem>()

        result.add(this[0].toChatItem())

        for (i in 1..lastIndex) {
            if (this[i].senderId == this[i - 1].senderId)
                result.add(this[i].toChatItem(true))
            else
                result.add(this[i].toChatItem())
        }

        return result
    }
}