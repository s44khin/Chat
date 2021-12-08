package ru.s44khin.messenger.presentation.main.streams.tabs

import ru.s44khin.messenger.data.model.ResultStream

interface MenuHandler {

    fun subscribe(streamName: String, description: String)

    fun unsubscribe(streamName: String)

    fun setStreamColor(streamId: Int, color: String)

    fun pinToTop(streamId: Int)

    fun unpinFromTop(streamId: Int)

    fun showMenu(stream: ResultStream)
}