package ru.s44khin.messenger.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.s44khin.messenger.data.dataBase.converters.ReactionConverter
import ru.s44khin.messenger.data.dataBase.converters.TopicConverter
import ru.s44khin.messenger.data.dataBase.dao.MessagesDao
import ru.s44khin.messenger.data.dataBase.dao.ProfileDao
import ru.s44khin.messenger.data.dataBase.dao.StreamsDao
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.model.ResultStream

@Database(
    entities = [Profile::class, ResultStream::class, Message::class],
    version = 1
)
@TypeConverters(TopicConverter::class, ReactionConverter::class)
abstract class MessengerDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    abstract fun streamsDao(): StreamsDao

    abstract fun messagesDao(): MessagesDao
}