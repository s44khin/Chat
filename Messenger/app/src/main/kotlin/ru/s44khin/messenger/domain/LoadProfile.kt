package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.dataBase.dao.ProfileDao
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.utils.MY_ID

class LoadProfile(
    private val repository: ZulipRepository,
    private val dataBase: ProfileDao
) {

    fun fromNetwork() = repository.getSelfProfile()

    fun fromDataBase() = dataBase.getById(MY_ID)

    fun saveToDataBase(profile: Profile) = dataBase.insert(profile)
}