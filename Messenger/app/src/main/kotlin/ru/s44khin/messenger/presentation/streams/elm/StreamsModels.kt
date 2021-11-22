package ru.s44khin.messenger.presentation.streams.elm

import ru.s44khin.messenger.data.model.Profile

data class State(
    val profile: Profile? = null
)

sealed class Event {
    sealed class Ui : Event() {
        object LoadProfileNetwork : Ui()
        object LoadProfileDB : Ui()
    }

    sealed class Internal : Event() {
        data class ProfileLoadedNetwork(val profile: Profile) : Internal()
        data class ProfileLoadedDB(val profile: Profile) : Internal()
        data class Error(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    data class ProfileLoadError(val error: Throwable) : Effect()
}

sealed class Command {
    object LoadProfileNetwork : Command()
    object LoadProfileDB : Command()
}