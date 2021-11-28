package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.utils.MY_ID

class LoadProfile(
    private val repository: ZulipRepository,
    private val dataBase: MessengerDataBase
) {

    fun fromNetwork() = repository.getSelfProfile()

    fun fromDataBase() = dataBase.profileDao().getById(MY_ID)

    fun saveToDataBase(profile: Profile) = dataBase.profileDao().insert(profile)

    fun createStream(name: String, description: String) = repository.createStream(name, description)
}