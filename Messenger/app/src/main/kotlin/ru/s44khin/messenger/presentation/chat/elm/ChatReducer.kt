package ru.s44khin.messenger.presentation.chat.elm

import ru.s44khin.messenger.data.network.api.UserInfo
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.utils.parse
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChatReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {

        is Event.Internal.ErrorLoadingNetwork -> {
            state { copy(error = event.error, isLoadingNetwork = false) }
        }

        is Event.Internal.ErrorLoadingDB -> {
            state { copy(isLoadingDB = true, isLoadingNetwork = true) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Internal.ErrorSendMessage -> {
            state { copy(error = event.error) }
        }

        is Event.Internal.ErrorDeleteMessage -> {
            state { copy() }
        }

        is Event.Internal.EditMessageError -> {
            state { copy() }
        }

        is Event.Internal.ReactionAddError -> {
            state { copy(error = event.error) }
        }

        is Event.Internal.ReactionRemoveError -> {
            state { copy(error = event.error) }
        }

        is Event.Internal.ErrorSendPicture -> {
            state { copy(error = event.error) }
        }

        is Event.Internal.MessagesLoadedNetwork -> {
            state {
                copy(
                    messages = event.messages,
                    isLoadingNetwork = false,
                    isLoadingDB = false,
                    error = null,
                    pageNumber = state.pageNumber + 1
                )
            }
        }

        is Event.Internal.MessagesLoadedDB -> {
            state { copy(messages = event.messages, isLoadingDB = false) }
        }

        is Event.Internal.MessageSent -> {
            state {
                val newMessages = state.messages?.toMutableList() ?: mutableListOf()
                val currentTime = parse((System.currentTimeMillis() / 1000).toInt())

                newMessages.add(
                    ChatItem(
                        id = UserInfo.ID,
                        topicName = event.topicName,
                        time = currentTime,
                        content = event.content,
                        profile = UserInfo.NAME,
                        avatar = UserInfo.AVATAR,
                        email = UserInfo.EMAIL,
                        isMyMessage = true,
                        reactions = mutableListOf()
                    )
                )

                copy(messages = newMessages)
            }
        }

        is Event.Internal.PictureSent -> {
            state { copy(imageUri = event.uri) }
            effects { Effect.ImageIsSend }
        }

        is Event.Internal.ReactionAdded -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Internal.MessageTopicChanged -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Internal.ReactionRemoved -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Internal.MessageDeleted -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Internal.MessageEdited -> {
            state { copy(isLoadingNetwork = true) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Ui.LoadNextPage -> {
            state { copy(isLoadingNetwork = true, error = null) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Ui.LoadMessagesDB -> {
            state { copy(isLoadingDB = true, isLoadingNetwork = true, error = null) }
            commands { +Command.LoadMessagesDB }
        }

        is Event.Ui.SendMessage -> {
            commands { +Command.SendMessage(event.content) }
        }

        is Event.Ui.SendMessageToTopic -> {
            commands { +Command.SendMessageToTopic(event.content, event.topicName) }
        }

        is Event.Ui.EditMessageTopic -> {
            commands { +Command.EditMessageTopic(event.id, event.topicName) }
        }

        is Event.Ui.AddReaction -> {
            commands { +Command.AddReaction(event.messageId, event.emojiName) }
        }

        is Event.Ui.RemoveReaction -> {
            commands { +Command.RemoveReaction(event.messageId, event.emojiName) }
        }

        is Event.Ui.DeleteMessage -> {
            commands { +Command.DeleteMessage(event.id) }
        }

        is Event.Ui.EditMessage -> {
            commands { +Command.EditMessage(event.id, event.content) }
        }

        is Event.Ui.SendPicture -> {
            effects { Effect.SendingImage }
            commands { +Command.SendPicture(event.filePart) }
        }
    }
}