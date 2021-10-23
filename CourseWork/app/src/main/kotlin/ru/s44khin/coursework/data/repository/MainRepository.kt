package ru.s44khin.coursework.data.repository

import ru.s44khin.coursework.data.model.Message
import ru.s44khin.coursework.data.model.Profile
import ru.s44khin.coursework.data.model.Stream
import ru.s44khin.coursework.data.model.Topic

class MainRepository {

    fun getMessages() = mutableListOf<Message>().apply {
        add(
            Message(
                date = 1609372800,
                avatar = "",
                profile = "Vasya Pupkin",
                message = "Hello World))))))))))))))))",
                reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12),
                alignment = 0
            )
        )

        add(
            Message(
                date = 1609372800,
                avatar = "",
                profile = "Vasya Pupkin",
                message = "Hello World ( 1 )",
                reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12),
                alignment = 0
            )
        )

        add(
            Message(
                date = 1609372800,
                avatar = "",
                profile = "Petya Petkin",
                message = "Hello World ( 2 )",
                reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12),
                alignment = 1
            )
        )

        add(
            Message(
                date = 1609372800,
                avatar = "",
                profile = "Vasya Pupkin",
                message = "Hello World ( 3 )",
                reactions = mutableListOf(),
                alignment = 0
            )
        )

        add(
            Message(
                date = 1609372800,
                avatar = "",
                profile = "Petya Petkin",
                message = "Hello World ( 4 ) Hello World ( 4 ) Hello World ( 4 ) Hello World ( 4 )",
                reactions = mutableListOf("🤣" to 2, "😀" to 3, "💕" to 7, "😢" to 12),
                alignment = 1
            )
        )
    }

    fun getProfile() = Profile(
        "Vasya Pypkin",
        "Im a meeting",
        "https://www.meme-arsenal.com/memes/a5dd2f55b36488a10172f4f84352846b.jpg",
        false,
        "vasyaPypkin@tinkoff.ru"
    )

    fun getPeoples() = mutableListOf(
        Profile(
            name = "Petya Petkin",
            status = "Im a meeting",
            online = true,
            avatar = "https://i.ytimg.com/vi/g74YJykOH7U/hqdefault.jpg",
            email = "petka1337@tinkoff.ru"
        ),

        Profile(
            name = "Oleg Tinkov",
            status = "Sleep",
            online = false,
            avatar = "https://gazetabiznes.ru/wp-content/uploads/2020/04/%D1%82%D0%B8%D0%BD%D1%8C%D0%BA%D0%BE%D1%84-%D0%BB%D0%B5%D0%B8%CC%86%D0%BA%D0%B5%D0%BC%D0%B8%D1%8F.jpg?v=1585934334",
            email = "tinkov@tinkoff.ru"
        )
    )

    fun getSubsStreams() = mutableListOf(
        Stream(
            name = "#general",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = getMessages()
                ),
                Topic(
                    name = "Bruh",
                    messages = getMessages()
                )
            )
        ),
        Stream(
            name = "#Development",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = getMessages()
                ),
                Topic(
                    name = "Bruh",
                    messages = getMessages()
                )
            )
        )
    )

    fun getAllStreams() = mutableListOf(
        Stream(
            name = "#general",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = getMessages()
                ),
                Topic(
                    name = "Bruh",
                    messages = getMessages()
                )
            )
        ),
        Stream(
            name = "#Development",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = getMessages()
                ),
                Topic(
                    name = "Bruh",
                    messages = getMessages()
                )
            )
        ),
        Stream(
            name = "#Design",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = getMessages()
                ),
                Topic(
                    name = "Bruh",
                    messages = getMessages()
                )
            )
        ),
        Stream(
            name = "#PR",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = getMessages()
                ),
                Topic(
                    name = "Bruh",
                    messages = getMessages()
                )
            )
        ),
    )
}