package ru.s44khin.messenger.presentation.streams.elm

import ru.s44khin.messenger.data.model.Profile

data class State(
    val profile: Profile? = null,
    val error: Throwable? = null,
    val isLoadingNetwork: Boolean = false,
    val isLoadingDB: Boolean = false
)

sealed class Event {

    sealed class Ui : Event() {
        object LoadProfileNetwork : Ui()
        object LoadProfileDB : Ui()
        data class CreateStream(val name: String, val description: String) : Ui()
    }

    sealed class Internal : Event() {
        data class ProfileLoadedNetwork(val profile: Profile) : Internal()
        data class ProfileLoadedDB(val profile: Profile) : Internal()
        object StreamCreated : Internal()
        data class ErrorLoadingNetwork(val error: Throwable) : Internal()
        data class ErrorLoadingDataBase(val error: Throwable?) : Internal()
        data class ErrorCreateStream(val error: Throwable) : Internal()
    }
}

sealed class Effect {

    data class ProfileLoadError(val error: Throwable) : Effect()
    data class CreateStreamError(val error: Throwable) : Effect()
    object StreamCreated : Effect()
}

sealed class Command {

    object LoadProfileNetwork : Command()
    object LoadProfileDB : Command()
    data class CreateStream(val name: String, val description: String) : Command()
}