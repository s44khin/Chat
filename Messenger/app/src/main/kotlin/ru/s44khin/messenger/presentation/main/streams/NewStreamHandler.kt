package ru.s44khin.messenger.presentation.main.streams

interface NewStreamHandler {

    fun createNewStream(name: String, description: String)
}