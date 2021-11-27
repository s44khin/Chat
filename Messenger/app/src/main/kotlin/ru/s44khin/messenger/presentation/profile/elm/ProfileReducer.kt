package ru.s44khin.messenger.presentation.profile.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event): Any = when (event) {

        is Event.Internal.ErrorLoadingNetwork -> {
            state { copy(error = event.error, isLoadingNetwork = false) }
            effects { Effect.ProfileLoadError(event.error) }
        }

        is Event.Internal.ErrorLoadingDataBase -> {
            state { copy(isLoadingDB = false, isLoadingNetwork = true, error = null) }
            commands { +Command.LoadProfileNetwork }
        }

        is Event.Internal.ProfileLoadedNetwork -> {
            state {
                copy(
                    profile = event.profile,
                    isLoadingNetwork = false,
                    isLoadingDB = false,
                    error = null
                )
            }
        }

        is Event.Internal.ProfileLoadedDB -> {
            state {
                copy(
                    profile = event.profile,
                    isLoadingDB = false,
                    isLoadingNetwork = true,
                    error = null
                )
            }
            commands { +Command.LoadProfileNetwork }
        }

        is Event.Ui.LoadProfileFirst -> if (state.profile != null) {
            state { copy(profile = state.profile, isLoadingNetwork = false, isLoadingDB = false) }
        } else {
            state { copy(isLoadingDB = true, isLoadingNetwork = true) }
            commands { +Command.LoadProfileDB }
        }

        is Event.Ui.LoadProfileNetwork -> {
            state { copy(isLoadingNetwork = true, error = null) }
            commands { +Command.LoadProfileNetwork }
        }
    }
}