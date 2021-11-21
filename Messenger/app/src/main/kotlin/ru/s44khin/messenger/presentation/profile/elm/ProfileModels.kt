package ru.s44khin.messenger.presentation.profile.elm

import ru.s44khin.messenger.data.model.Profile

data class State(
    val profile: Profile? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
)

sealed class Event {
    sealed class Ui : Event() {
        object LoadProfile: Ui()
    }

    sealed class Internal : Event() {
        data class ProfileLoaded(val profile: Profile) : Internal()
        data class ErrorLoading(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    data class ProfileLoadError(val error: Throwable) : Effect()
}

sealed class Command {
    object LoadProfile : Command()
}