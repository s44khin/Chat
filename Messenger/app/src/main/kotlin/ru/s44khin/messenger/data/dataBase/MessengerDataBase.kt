package ru.s44khin.messenger.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.s44khin.messenger.data.dataBase.converters.TopicConverter
import ru.s44khin.messenger.data.dataBase.dao.ProfileDao
import ru.s44khin.messenger.data.dataBase.dao.StreamsDao
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.model.ResultStream

@Database(
    entities = [
        Profile::class,
        ResultStream::class
    ],
    version = 1
)
@TypeConverters(TopicConverter::class)
abstract class MessengerDataBase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    abstract fun streamsDao(): StreamsDao
}