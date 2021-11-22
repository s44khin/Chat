package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.dataBase.dao.ProfileDao
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.network.ZulipRepository

class LoadMembers(
    private val repository: ZulipRepository,
    private val dataBase: ProfileDao
) {

    fun fromNetwork() = repository.getMembers()

    fun fromDataBase() = dataBase.getAll()

    fun saveToDataBase(members: List<Profile>) = dataBase.insertAll(members)
}