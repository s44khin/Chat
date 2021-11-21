package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.network.ZulipRepository

class LoadAllStreams(
    private val repository: ZulipRepository
) {

    fun execute() = repository.getAllStreams()
}