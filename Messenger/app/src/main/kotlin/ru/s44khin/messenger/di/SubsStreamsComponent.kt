package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.domain.LoadSubsStreams
import ru.s44khin.messenger.domain.LoadTopics
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.*
import vivid.money.elmslie.core.ElmStoreCompat

@Component(modules = [SubsStreamsModule::class])
interface SubsStreamsComponent {

    val subsStreamsStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [AppModule::class])
class SubsStreamsModule {

    @Provides
    fun provideLoadSubsStreams(
        repository: ZulipRepository,
        dataBase: MessengerDataBase
    ) = LoadSubsStreams(repository, dataBase)

    @Provides
    fun provideLoadTopics(repository: ZulipRepository) = LoadTopics(repository)

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

