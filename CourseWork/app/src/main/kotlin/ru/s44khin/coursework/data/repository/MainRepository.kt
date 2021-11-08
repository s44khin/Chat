package ru.s44khin.coursework.data.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.s44khin.coursework.data.model.Message
import ru.s44khin.coursework.data.model.Profile
import ru.s44khin.coursework.data.model.Stream
import ru.s44khin.coursework.data.model.Topic
import java.util.concurrent.TimeUnit

class MainRepository {

    private val range = 1000L..5000L
    private val messages = mutableListOf<Message>().apply {
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
    private val subsStreams = mutableListOf(
        Stream(
            name = "#general",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = messages
                ),
                Topic(
                    name = "Bruh",
                    messages = messages
                )
            )
        ),
        Stream(
            name = "#Development",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = messages
                ),
                Topic(
                    name = "Bruh",
                    messages = messages
                )
            )
        )
    )
    private val allStreams = mutableListOf(
        Stream(
            name = "#general",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = messages
                ),
                Topic(
                    name = "Bruh",
                    messages = messages
                )
            )
        ),
        Stream(
            name = "#Development",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = messages
                ),
                Topic(
                    name = "Bruh",
                    messages = messages
                )
            )
        ),
        Stream(
            name = "#Design",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = messages
                ),
                Topic(
                    name = "Bruh",
                    messages = messages
                )
            )
        ),
        Stream(
            name = "#PR",
            topics = listOf(
                Topic(
                    name = "Testing",
                    messages = messages
                ),
                Topic(
                    name = "Bruh",
                    messages = messages
                )
            )
        ),
    )
    private val profile1 = Profile(
        "Vasya Pypkin",
        "Im a meeting",
        "https://www.meme-arsenal.com/memes/a5dd2f55b36488a10172f4f84352846b.jpg",
        false,
        "vasyaPypkin@tinkoff.ru"
    )
    private val profile2 = Profile(
        name = "Oleg Tinkov",
        status = "Sleep",
        online = false,
        avatar = "https://gazetabiznes.ru/wp-content/uploads/2020/04/%D1%82%D0%B8%D0%BD%D1%8C%D0%BA%D0%BE%D1%84-%D0%BB%D0%B5%D0%B8%CC%86%D0%BA%D0%B5%D0%BC%D0%B8%D1%8F.jpg?v=1585934334",
        email = "tinkov@tinkoff.ru"
    )

    fun getMessages(): Single<MutableList<Message>> {
        return Single.fromCallable { messages }
            .delay(range.random(), TimeUnit.MILLISECONDS)
    }

    fun getProfile(): Single<Profile> {
        return Single.fromCallable { profile1 }
            .delay(range.random(), TimeUnit.MILLISECONDS)
    }

    fun getPeople(): Single<List<Profile>> {
        return Single.fromCallable { listOf(profile1, profile2) }
            .delay(range.random(), TimeUnit.MILLISECONDS)
    }

    fun getSubsStreams(text: String = ""): Observable<List<Stream>> {
        return Observable.fromCallable() { subsStreams }
            .delay(range.random(), TimeUnit.MILLISECONDS)
            .map { streams -> streams.filter { it.name.contains(text, true) } }
    }

    fun getAllStreams(text: String = ""): Observable<List<Stream>> {
        return Observable.fromCallable() { allStreams }
            .delay(range.random(), TimeUnit.MILLISECONDS)
            .map { streams -> streams.filter { it.name.contains(text, true) } }
    }
}