package ru.s44khin.messenger.presentation.chat.pagination

class Paginator {

    var currentPage: Int = 0
        private set
    val isFirstPage
        get() = currentPage == 0

    fun pageLoaded() {
        currentPage++
    }

    fun reset() {
        currentPage = 0
    }
}