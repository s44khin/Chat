package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.domain.LoadMembers
import ru.s44khin.messenger.presentation.main.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton


@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {

    val mainStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [LoadModule::class])
class MainModule {

    @Singleton
    @Provides
    fun provideMainStore(loadMembers: LoadMembers) = ElmStoreCompat(
        initialState = State(),
        reducer = MainReducer(),
        actor = MainActor(loadMembers)
    )
}