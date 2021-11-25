package ru.s44khin.messenger.presentation.chat.pagination

class PaginationHandler(
    val paginator: Paginator,
) {

    fun process(error: Throwable, viewCallback: (message: String, isCritical: Boolean) -> Unit) {
        viewCallback("error", paginator.isFirstPage)
    }
}