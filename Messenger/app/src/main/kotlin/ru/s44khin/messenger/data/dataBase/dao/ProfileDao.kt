package ru.s44khin.messenger.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ru.s44khin.messenger.data.model.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile")
    fun getAll(): Single<List<Profile>>

    @Query("SELECT * FROM profile WHERE user_id = :id")
    fun getById(id: Int): Single<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(profiles: List<Profile>)
}
