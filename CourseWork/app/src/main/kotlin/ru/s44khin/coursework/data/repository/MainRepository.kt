package ru.s44khin.coursework.data.repository

import ru.s44khin.coursework.data.model.Message

class MainRepository {

    fun getMessages() = mutableListOf<Message>().apply {
        add(Message(
            date = 1609372800,
            avatar = "",
            profile = "Vasya Pupkin",
            message = "Hello World))))))))))))))))",
            reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12,"😢" to 12),
            alignment = 0
        ))

        add(Message(
            date = 1609372800,
            avatar = "",
            profile = "Vasya Pupkin",
            message = "Hello World ( 1 )",
            reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12,"😢" to 12),
            alignment = 0
        ))

        add(Message(
            date = 1609372800,
            avatar = "",
            profile = "Petya Petkin",
            message = "Hello World ( 2 )",
            reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12,"😢" to 12),
            alignment = 1
        ))

        add(Message(
            date = 1609372800,
            avatar = "",
            profile = "Vasya Pupkin",
            message = "Hello World ( 3 )",
            reactions = mutableListOf(),
            alignment = 0
        ))

        add(Message(
            date = 1609372800,
            avatar = "",
            profile = "Petya Petkin",
            message = "Hello World ( 4 ) Hello World ( 4 ) Hello World ( 4 ) Hello World ( 4 )",
            reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12,"😢" to 12),
            alignment = 1
        ))
    }
}