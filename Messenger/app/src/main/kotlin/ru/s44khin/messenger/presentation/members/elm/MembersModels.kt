package ru.s44khin.messenger.presentation.members.elm

import ru.s44khin.messenger.data.model.Profile

data class State(
    val members: List<Profile>? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = false
)

sealed class Event {
    sealed class Ui : Event() {
        object LoadMembersNetwork : Ui()
        object LoadMembersDB: Ui()
    }

    sealed class Internal : Event() {
        data class MembersLoadedNetwork(val members: List<Profile>) : Internal()
        data class MembersLoadedDB(val members: List<Profile>) : Internal()
        data class ErrorLoadingNetwork(val error: Throwable) : Internal()
        data class ErrorLoadingDataBase(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    data class MembersLoadError(val error: Throwable) : Effect()
}

sealed class Command {
    object LoadMembersNetwork : Command()
    object LoadMembersDB : Command()
}