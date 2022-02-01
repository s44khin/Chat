package ru.s44khin.messenger.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.domain.LoadMessages

@ScreenScope
@Component(
    modules = [ChatModule::class],
    dependencies = [AppComponent::class]
)
interface ChatComponent {

    val loadMessages: LoadMessages

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): ChatComponent
    }
}

@Module
class ChatModule {

    @Provides
    fun provideLoadMessages(
        repository: ZulipRepository,
        database: MessengerDatabase
    ) = LoadMessages(repository, database)
}