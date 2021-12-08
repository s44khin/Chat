package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.domain.LoadAllStreams
import ru.s44khin.messenger.domain.LoadTopics
import ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton

@Singleton
@Component(modules = [AllStreamsModule::class])
interface AllStreamsComponent {

    val allStreamsStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [LoadModule::class])
class AllStreamsModule {

    @Singleton
    @Provides
    fun provideAllStreamsStore(
        loadAllStreams: LoadAllStreams,
        loadTopics: LoadTopics
    ) = ElmStoreCompat(
        initialState = State(),
        reducer = AllStreamsReducer(),
        actor = AllStreamsActor(loadAllStreams, loadTopics)
    )
}