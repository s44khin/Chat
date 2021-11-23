package ru.s44khin.messenger.presentation.chat.elm

import vivid.money.elmslie.core.ElmStoreCompat

class ChatStoryFactory(
    private val actor: ChatActor,
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = State(),
            reducer = ChatReducer(),
            actor = actor
        )
    }

    fun provide() = store
}