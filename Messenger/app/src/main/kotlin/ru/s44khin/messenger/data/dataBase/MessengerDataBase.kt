package ru.s44khin.messenger.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.s44khin.messenger.data.model.Profile

@Database(
    entities = [Profile::class],
    version = 1
)
abstract class MessengerDataBase: RoomDatabase() {

    abstract fun profileDao(): ProfileDao
}