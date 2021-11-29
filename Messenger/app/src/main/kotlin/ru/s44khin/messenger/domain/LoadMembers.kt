package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDatabase
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.utils.MY_ID

class LoadMembers(
    private val repository: ZulipRepository,
    private val dataBase: MessengerDatabase
) {

    fun fromNetwork() = repository.getMembers()

    fun getSelfProfileFromNetwork() = repository.getSelfProfile()

    fun fromDatabase() = dataBase.profileDao().getAll()

    fun getSelfProfileFromDatabase() = dataBase.profileDao().getById(MY_ID)

    fun saveToDataBase(members: List<Profile>) = dataBase.profileDao().insertAll(members)
}