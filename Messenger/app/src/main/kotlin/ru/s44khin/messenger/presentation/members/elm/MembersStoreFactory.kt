package ru.s44khin.messenger.presentation.members.elm

import vivid.money.elmslie.core.ElmStoreCompat

class MembersStoreFactory(
    private val actor: MembersActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = State(),
            reducer = MembersReducer(),
            actor = actor
        )
    }

    fun provide() = store
}