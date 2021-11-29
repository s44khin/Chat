package ru.s44khin.messenger.di

import androidx.room.Room
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.data.network.api.RequestManager
import ru.s44khin.messenger.domain.LoadAllStreams
import ru.s44khin.messenger.domain.LoadMembers
import ru.s44khin.messenger.domain.LoadSubsStreams
import ru.s44khin.messenger.domain.LoadTopics
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    val repository: ZulipRepository
    val database: MessengerDatabase
}

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRepository() = ZulipRepository(RequestManager.service)

    @Singleton
    @Provides
    fun provideDatabase() = Room.databaseBuilder(
        MessengerApplication.instance,
        MessengerDatabase::class.java,
        "Database"
    ).build()
}

@Module(includes = [AppModule::class])
class LoadModule {

    @Singleton
    @Provides
    fun provideLoadAllStreams(
        repository: ZulipRepository,
        database: MessengerDatabase
    ) = LoadAllStreams(repository, database)

    @Singleton
    @Provides
    fun provideLoadSubsStreams(
        repository: ZulipRepository,
        database: MessengerDatabase
    ) = LoadSubsStreams(repository, database)

    @Singleton
    @Provides
    fun provideLoadTopics(repository: ZulipRepository) = LoadTopics(repository)

    @Singleton
    @Provides
    fun provideLoadMembers(
        repository: ZulipRepository,
        database: MessengerDatabase
    ) = LoadMembers(repository, database)
}