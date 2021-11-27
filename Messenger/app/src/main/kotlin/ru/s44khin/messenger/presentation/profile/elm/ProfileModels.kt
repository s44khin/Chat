package ru.s44khin.messenger.presentation.profile.elm

import ru.s44khin.messenger.data.model.Profile

data class State(
    val profile: Profile? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = true
)

sealed class Event {

    sealed class Ui : Event() {
        object LoadProfileFirst : Ui()
        object LoadProfileNetwork : Ui()
    }

    sealed class Internal : Event() {
        data class ProfileLoadedNetwork(val profile: Profile) : Internal()
        data class ProfileLoadedDB(val profile: Profile) : Internal()
        data class ErrorLoadingNetwork(val error: Throwable) : Internal()
        data class ErrorLoadingDataBase(val error: Throwable?) : Internal()
    }
}

sealed class Effect {

    data class ProfileLoadError(val error: Throwable) : Effect()
}

sealed class Command {

    object LoadProfileNetwork : Command()
    object LoadProfileDB : Command()
}