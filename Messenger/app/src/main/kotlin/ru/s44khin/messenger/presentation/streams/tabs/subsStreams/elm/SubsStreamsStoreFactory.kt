package ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm

import vivid.money.elmslie.core.ElmStoreCompat

class SubsStreamsStoreFactory(
    private val actor: SubsStreamsActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = State(),
            reducer = SubsStreamsReducer(),
            actor = actor
        )
    }

    fun provide() = store
}