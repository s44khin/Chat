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
    lateinit var mainComponent: MainComponent
    lateinit var allStreamsComponent: AllStreamsComponent
    lateinit var subsStreamsComponent: SubsStreamsComponent
    lateinit var membersComponent: MembersComponent
    lateinit var chatComponent: ChatComponent

    override fun onCreate() {
        super.onCreate()
        initDI()
        instance = this
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.create()
        mainComponent = DaggerMainComponent.create()
        allStreamsComponent = DaggerAllStreamsComponent.create()
        subsStreamsComponent = DaggerSubsStreamsComponent.create()
        membersComponent = DaggerMembersComponent.create()
        chatComponent = DaggerChatComponent.factory()
            .create(appComponent)
    }
}