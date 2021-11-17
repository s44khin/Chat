package ru.s44khin.messenger.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ru.s44khin.messenger.data.model.Topic
import ru.s44khin.messenger.ui.chat.ChatItem.Message

@Dao
interface MessagesDao {

    @Query("SELECT * FROM message WHERE topicName = :topicName")
    fun getAll(topicName: String): Single<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<Message>)
}