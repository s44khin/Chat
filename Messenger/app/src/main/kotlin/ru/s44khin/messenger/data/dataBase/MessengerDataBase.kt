package ru.s44khin.messenger.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.s44khin.messenger.data.model.AllStream
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.model.SubsStream

@Database(
    entities = [
        Profile::class,
        AllStream::class,
        SubsStream::class
    ],
    version = 1
)
abstract class MessengerDataBase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    abstract fun allStreamsDao(): AllStreamsDao

    abstract fun subsStreamsDao(): SubsStreamsDao
}