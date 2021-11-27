package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.domain.LoadProfile
import ru.s44khin.messenger.presentation.profile.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Inject

@Component(modules = [ProfileModule::class])
interface ProfileComponent {

    val profileStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [AppModule::class])
class ProfileModule {

    @Provides
    fun provideLoadProfile(
        repository: ZulipRepository,
        dataBase: MessengerDataBase
    ) = LoadProfile(repository, dataBase)

    @Provides
    fun provideProfileStore(loadProfile: LoadProfile) = ElmStoreCompat(
        initialState = State(),
        reducer = ProfileReducer(),
        actor = ProfileActor(loadProfile)
    )
}