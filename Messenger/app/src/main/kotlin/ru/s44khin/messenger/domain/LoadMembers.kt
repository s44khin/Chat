package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.network.ZulipRepository
import javax.inject.Inject

class LoadMembers(
    private var repository: ZulipRepository,
    private var dataBase: MessengerDataBase
) {

    fun fromNetwork() = repository.getMembers()

    fun fromDataBase() = dataBase.profileDao().getAll()

    fun saveToDataBase(members: List<Profile>) = dataBase.profileDao().insertAll(members)
}