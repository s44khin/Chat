package ru.s44khin.messenger.domain

import ru.s44khin.messenger.data.network.ZulipRepository

class LoadMembers(
    private val repository: ZulipRepository
) {

    fun execute() = repository.getMembers()
}