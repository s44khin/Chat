package ru.s44khin.messenger

import android.app.Application
import androidx.room.Room
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.data.network.api.RequestManager
import ru.s44khin.messenger.di.GlobalDI
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

    override fun onCreate() {
        super.onCreate()
        instance = this
        GlobalDI.init(this)
    }
}