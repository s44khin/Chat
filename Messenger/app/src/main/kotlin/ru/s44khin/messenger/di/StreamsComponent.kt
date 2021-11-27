package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.domain.LoadProfile
import ru.s44khin.messenger.presentation.streams.elm.*
import vivid.money.elmslie.core.ElmStoreCompat

@Component(modules = [StreamsModule::class])
interface StreamsComponent {

    val streamStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [ProfileModule::class])
class StreamsModule {

    @Provides
    fun provideStreamsStore(loadProfile: LoadProfile) = ElmStoreCompat(
        initialState = State(),
        reducer = StreamsReducer(),
        actor = StreamsActor(loadProfile)
    )
}