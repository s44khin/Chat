package ru.s44khin.messenger.presentation.chat.elm

import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.utils.*
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChatReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {

        is Event.Internal.ErrorLoadingNetwork -> {
            state { copy(error = event.error, isLoadingNetwork = false) }
            effects { Effect.MessagesLoadError(event.error) }
        }

        is Event.Internal.ErrorLoadingDB -> {
            state { copy(isLoadingDB = true, isLoadingNetwork = true) }
            commands { +Command.LoadPage(state.pageNumber) }
        }

        is Event.Internal.ErrorSendMessage -> {
            state { copy(error = event.error) }
        }

        is Event.Internal.ReactionAddError -> {
            state { copy(error = event.error) }
        }

        is Event.Internal.ReactionRemoveError -> {
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
                        id = MY_ID,
                        topicName = event.topicName,
                        time = currentTime,
                        content = event.content,
                        profile = MY_NAME,
                        avatar = MY_AVATAR,
                        email = MY_EMAIL,
                        isMyMessage = true,
                        reactions = mutableListOf()
                    )
                )

                copy(messages = newMessages)
            }
        }

        is Event.Internal.ReactionAdded -> {
            state { copy() }
        }

        is Event.Internal.ReactionRemoved -> {
            state { copy() }
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

        is Event.Ui.AddReaction -> {
            commands { +Command.AddReaction(event.messageId, event.emojiName) }
        }

        is Event.Ui.RemoveReaction -> {
            commands { +Command.RemoveReaction(event.messageId, event.emojiName) }
        }
    }
}