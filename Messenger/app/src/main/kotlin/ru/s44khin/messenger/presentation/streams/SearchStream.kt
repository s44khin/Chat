package ru.s44khin.messenger.presentation.streams

import io.reactivex.Observable

interface SearchStream {

    fun search(text: String)
}