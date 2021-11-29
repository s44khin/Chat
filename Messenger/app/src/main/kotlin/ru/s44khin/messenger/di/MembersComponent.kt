package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.domain.LoadMembers
import ru.s44khin.messenger.presentation.main.members.elm.*
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton

@Singleton
@Component(modules = [MembersModule::class])
interface MembersComponent {

    val membersStore: ElmStoreCompat<Event, State, Effect, Command>
}

@Module(includes = [LoadModule::class])
class MembersModule {

    @Singleton
    @Provides
    fun provideMembersStore(loadMembers: LoadMembers) = ElmStoreCompat(
        initialState = State(),
        reducer = MembersReducer(),
        actor = MembersActor(loadMembers)
    )
}