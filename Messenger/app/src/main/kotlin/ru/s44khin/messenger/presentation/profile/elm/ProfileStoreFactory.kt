package ru.s44khin.messenger.presentation.profile.elm

import vivid.money.elmslie.core.ElmStoreCompat

class ProfileStoreFactory(
    private val actor: ProfileActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = State(),
            reducer = ProfileReducer(),
            actor = actor
        )
    }

    fun provide() = store
}