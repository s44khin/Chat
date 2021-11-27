package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.domain.LoadMembers
import ru.s44khin.messenger.presentation.members.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton

@Singleton
@Component(modules = [MembersModule::class])
interface MembersComponent {

    val membersStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [AppModule::class])
class MembersModule {

    @Provides
    @Singleton
    fun provideLoadProfiles(
        repository: ZulipRepository,
        dataBase: MessengerDataBase
    ) = LoadMembers(repository, dataBase)

    @Provides
    @Singleton
    fun provideMembersStore(loadMembers: LoadMembers) = ElmStoreCompat(
        initialState = State(),
        reducer = MembersReducer(),
        actor = MembersActor(loadMembers)
    )
}