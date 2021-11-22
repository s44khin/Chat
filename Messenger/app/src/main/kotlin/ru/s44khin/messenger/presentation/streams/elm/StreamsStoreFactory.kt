package ru.s44khin.messenger.presentation.streams.elm

import vivid.money.elmslie.core.ElmStoreCompat

class StreamsStoreFactory(
    private val actor: StreamsActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = State(),
            reducer = StreamsReducer(),
            actor = actor
        )
    }

    fun provide() = store
}