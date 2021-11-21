package ru.s44khin.messenger.presentation.members.elm

import ru.s44khin.messenger.data.model.Profile

data class State(
    val members: List<Profile> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false
)

sealed class Event {
    sealed class Ui : Event() {
        object LoadMembers : Ui()
    }

    sealed class Internal : Event() {
        data class MembersLoaded(val members: List<Profile>) : Internal()
        data class ErrorLoading(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    data class MembersLoadError(val error: Throwable) : Effect()
}

sealed class Command {
    object LoadMembers : Command()
}