package ru.s44khin.messenger.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import ru.s44khin.messenger.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

const val MY_ID = 454991
const val MY_AVATAR =
    "https://zulip-avatars.s3.amazonaws.com/39154/76c4ab526496e0b37b446c6df4487bd41d499082?version=2"
const val MY_NAME = "Анохин Александр"

@SuppressLint("SimpleDateFormat")
fun parse(time: Int): String = SimpleDateFormat("d MMM").format(Date(time * 1000L))

lateinit var emojiList: List<Pair<String, String>>

fun getEmojis(resources: Resources): List<Pair<String, String>> {
    val result = mutableListOf<Pair<String, String>>()
    val inputStream = resources.openRawResource(R.raw.emojis)
    val reader = BufferedReader(InputStreamReader(inputStream))

    var line = reader.readLine()

    while (line != "#EOF") {
        val temp = line.split(":")
        result.add(temp[1] to temp[0])
        line = reader.readLine()
    }

    return result
}

fun hexToEmoji(string: String) = String(Character.toChars(string.toInt(16)))