package ru.s44khin.messenger.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ru.s44khin.messenger.data.model.ResultStream

@Dao
interface StreamsDao {

    @Query("SELECT * FROM stream WHERE subscription=:subscription")
    fun getAll(subscription: Boolean = false): Single<List<ResultStream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(streams: List<ResultStream>)
}