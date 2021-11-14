package ru.s44khin.messenger.data.model

data class ResultStream(
    val description: String,
    val name: String,
    val streamId: Int,
    val topics: List<Topic>
)