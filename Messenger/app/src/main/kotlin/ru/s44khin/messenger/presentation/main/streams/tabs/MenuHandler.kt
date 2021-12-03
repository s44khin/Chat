package ru.s44khin.messenger.presentation.main.streams.tabs

import ru.s44khin.messenger.data.model.ResultStream

interface MenuHandler {

    fun subscribe(streamName: String, description: String)

    fun unsubscribe(streamName: String)

    fun setStreamColor(streamId: Int, color: String)

    fun showMenu(stream: ResultStream)
}