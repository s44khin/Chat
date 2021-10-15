package ru.s44khin.coursework.data.repository

import ru.s44khin.coursework.data.model.Message

class MainRepository {

    fun getMessages(): List<Message> {
        val result = mutableListOf<Message>()
        val avatar = "https://offvkontakte.ru/wp-content/uploads/avatarka-pustaya-vk_20.jpg"

        return result.apply {
            add(
                Message(
                    avatar,
                    profile = "Vasya Pypkin",
                    message = "Hello Worldфывфыфвфывфывфывфывфывфывфывфывфывфывфывфыфыыв",
                    reactions = listOf("🤔" to 3, "❤️" to 1),
                    alignment = 0
                )
            )

            add(
                Message(
                    avatar,
                    profile = "Vasya Pypkin",
                    message = "Hello World2",
                    reactions = listOf(),
                    alignment = 0
                )
            )

            add(
                Message(
                    avatar,
                    profile = "Ivan Ivanov",
                    message = "Hello World3влоафывдоларфыдвоадфвыроафыврадфыврдларфывоардфывардфыв",
                    reactions = listOf("👌" to 3),
                    alignment = 1
                )
            )

            add(
                Message(
                    avatar,
                    profile = "Vasya Pypkin",
                    message = "Hello World",
                    reactions = listOf("❤️" to 1),
                    alignment = 0
                )
            )

            add(
                Message(
                    avatar,
                    profile = "Vasya Pypkin",
                    message = "Hello World",
                    reactions = listOf("🤔" to 3, "❤️" to 1),
                    alignment = 0
                )
            )

            add(
                Message(
                    avatar,
                    profile = "Vasya Pypkin",
                    message = "Hello World2",
                    reactions = listOf(),
                    alignment = 0
                )
            )

            add(
                Message(
                    avatar,
                    profile = "Ivan Ivanov",
                    message = "Hello World3",
                    reactions = listOf("👌" to 3),
                    alignment = 1
                )
            )

            add(
                Message(
                    avatar,
                    profile = "Vasya Pypkin",
                    message = "Hello World",
                    reactions = listOf("❤️" to 1),
                    alignment = 0
                )
            )
        }
    }
}