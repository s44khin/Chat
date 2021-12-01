package ru.s44khin.messenger.presentation.main.streams.tabs

interface PopupMenuHandler {

    fun subscribe(streamName: String, description: String)

    fun unsubscribe(streamName: String)
}