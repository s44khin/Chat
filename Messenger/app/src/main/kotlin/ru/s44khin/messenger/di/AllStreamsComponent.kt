package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.domain.LoadAllStreams
import ru.s44khin.messenger.domain.LoadTopics
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Singleton

@Singleton
@Component(modules = [AllStreamModule::class])
interface AllStreamsComponent {

    val allStreamStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [AppModule::class])
class AllStreamModule {

    @Provides
    fun provideLoadAllStreams(
        repository: ZulipRepository,
        dataBase: MessengerDataBase
    ) = LoadAllStreams(repository, dataBase)

    @Provides
    @Singleton
    fun provideLoadTopics(repository: ZulipRepository) = LoadTopics(repository)

    @Provides
    @Singleton
    fun provideAllStreamsStore(
        loadAllStreams: LoadAllStreams,
        loadTopics: LoadTopics
    ) = ElmStoreCompat(
        initialState = State(),
        reducer = AllStreamsReducer(),
        actor = AllStreamsActor(loadAllStreams, loadTopics)
    )
}

