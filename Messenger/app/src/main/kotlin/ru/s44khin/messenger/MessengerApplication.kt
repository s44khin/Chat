package ru.s44khin.messenger

import android.app.Application
import ru.s44khin.messenger.di.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MessengerApplication : Application() {

    companion object {
        lateinit var instance: MessengerApplication
    }

    val emojiList: List<Pair<String, String>> by lazy {
        val result = mutableListOf<Pair<String, String>>()
        val inputStream = resources.openRawResource(R.raw.emojis)
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line = reader.readLine()

        while (line != "#EOF") {
            val temp = line.split(":")
            result.add(temp[1] to temp[0])
            line = reader.readLine()
        }

        result
    }

    lateinit var appComponent: AppComponent
    lateinit var memberComponent: MembersComponent
    lateinit var allStreamsComponent: AllStreamsComponent
    lateinit var subsStreamsComponent: SubsStreamsComponent
    lateinit var streamsComponent: StreamsComponent
    lateinit var chatComponent: ChatComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.create()
        memberComponent = DaggerMembersComponent.create()
        allStreamsComponent = DaggerAllStreamsComponent.create()
        subsStreamsComponent = DaggerSubsStreamsComponent.create()
        streamsComponent = DaggerStreamsComponent.create()
        chatComponent = DaggerChatComponent.create()

        instance = this
    }
}