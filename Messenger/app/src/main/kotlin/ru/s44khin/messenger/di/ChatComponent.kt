package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.domain.LoadMessages
import javax.inject.Singleton

@Singleton
@Component(modules = [ChatModule::class])
interface ChatComponent {
    val loadMessages: LoadMessages
}

@Module(includes = [AppModule::class])
class ChatModule {

    @Singleton
    @Provides
    fun provideLoadMessages(
        repository: ZulipRepository,
        database: MessengerDatabase
    ) = LoadMessages(repository, database)
}