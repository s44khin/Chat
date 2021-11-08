package ru.s44khin.coursework.data.model

data class Message(
    val date: Int,
    val avatar: String,
    val profile: String,
    val message: String,
    val reactions: MutableList<Pair<String, Int>>,
    val alignment: Int,
)