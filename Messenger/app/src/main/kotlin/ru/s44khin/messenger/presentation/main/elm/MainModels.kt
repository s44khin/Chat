package ru.s44khin.messenger.presentation.main.elm

import ru.s44khin.messenger.data.model.Profile

data class State(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)

sealed class Event {

    sealed class Ui : Event() {
        object LoadProfile : Ui()
    }

    sealed class Internal : Event() {

        data class ProfileLoadedDatabase(val profile: Profile) : Internal()

        data class ProfileLoadedNetwork(val profile: Profile) : Internal()

        data class ErrorLoadingDatabase(val error: Throwable?) : Internal()

        data class ErrorLoadingNetwork(val error: Throwable?) : Internal()
    }
}

sealed class Effect

sealed class Command {

    object LoadProfileDatabase : Command()

    object LoadProfileNetwork : Command()
}