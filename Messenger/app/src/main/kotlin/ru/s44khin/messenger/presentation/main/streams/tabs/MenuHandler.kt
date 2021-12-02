package ru.s44khin.messenger.presentation.main.streams.tabs

interface MenuHandler {

    fun subscribe(streamName: String, description: String)

    fun unsubscribe(streamName: String)

    fun setStreamColor(streamId: Int, color: String)

    fun showMenu(
        streamId: Int,
        name: String,
        date: String,
        description: String,
        color: String? = null
    )
}