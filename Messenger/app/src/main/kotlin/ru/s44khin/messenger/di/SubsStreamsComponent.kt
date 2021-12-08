package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.domain.LoadSubsStreams
import ru.s44khin.messenger.domain.LoadTopics
import ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton

@Singleton
@Component(modules = [SubsStreamsModule::class])
interface SubsStreamsComponent {

    val subsStreamsStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [LoadModule::class])
class SubsStreamsModule {

    @Singleton
    @Provides
    fun provideSubsStreamsStore(
        loadSubsStreams: LoadSubsStreams,
        loadTopics: LoadTopics
    ) = ElmStoreCompat(
        initialState = State(),
        reducer = SubsStreamsReducer(),
        actor = SubsStreamsActor(loadSubsStreams, loadTopics)
    )
}