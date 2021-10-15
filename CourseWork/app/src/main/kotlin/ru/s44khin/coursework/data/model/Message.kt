package ru.s44khin.coursework.data.model

import java.time.LocalDate

data class Message(
    val avatar: String,
    val profile: String,
    val message: String,
    val reactions: List<Pair<String, Int>>,
    val alignment: Int,
)