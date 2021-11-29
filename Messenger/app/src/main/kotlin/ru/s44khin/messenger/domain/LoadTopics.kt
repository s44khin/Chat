package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.network.ZulipRepository

class LoadTopics(
    private val repository: ZulipRepository
) {

    fun execute(streamId: Int) = repository.getTopics(streamId)
}
