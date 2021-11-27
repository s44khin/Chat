package ru.s44khin.messenger.di

import androidx.room.Room
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.data.network.api.RequestManager
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    val repository: ZulipRepository
    val dataBase: MessengerDataBase
}

@Module
class AppModule {

    @Provides
    fun provideRepository() = ZulipRepository(RequestManager.service)

    @Provides
    fun provideDB() = Room.databaseBuilder(
        MessengerApplication.instance,
        MessengerDataBase::class.java,
        "DataBase"
    ).build()
}