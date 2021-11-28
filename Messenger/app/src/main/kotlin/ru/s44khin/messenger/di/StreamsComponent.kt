package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.domain.LoadProfile
import ru.s44khin.messenger.presentation.streams.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton

@Singleton
@Component(modules = [StreamsModule::class])
interface StreamsComponent {

    val streamStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [AppModule::class])
class StreamsModule {

    @Provides
    @Singleton
    fun provideLoadProfile(
        repository: ZulipRepository,
        dataBase: MessengerDataBase
    ) = LoadProfile(repository, dataBase)

    @Provides
    @Singleton
    fun provideStreamsStore(loadProfile: LoadProfile) = ElmStoreCompat(
        initialState = State(),
        reducer = StreamsReducer(),
        actor = StreamsActor(loadProfile)
    )
}