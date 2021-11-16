package ru.s44khin.messenger.data.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single
import ru.s44khin.messenger.data.model.AllStream
import ru.s44khin.messenger.data.model.ResultStream

@Dao
interface AllStreamsDao {

    @Query("SELECT * FROM AllStream")
    fun getAll(): Single<List<AllStream>>

    @Insert
    fun insertAll(streams: List<AllStream>)
}