package ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm

import vivid.money.elmslie.core.ElmStoreCompat

class AllStreamsStoreFactory(
    private val actor: AllStreamsActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = State(),
            reducer = AllStreamsReducer(),
            actor = actor
        )
    }

    fun provide() = store
}