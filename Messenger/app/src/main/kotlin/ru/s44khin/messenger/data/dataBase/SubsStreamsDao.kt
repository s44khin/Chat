package ru.s44khin.messenger.data.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.model.SubsStream

@Dao
interface SubsStreamsDao {

    @Query("SELECT * FROM AllStream")
    fun getAll(): Single<List<SubsStream>>

    @Insert
    fun insertAll(streams: List<SubsStream>)
}