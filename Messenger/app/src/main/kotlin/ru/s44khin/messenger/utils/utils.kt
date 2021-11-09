package ru.s44khin.messenger.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

const val MY_ID = 454991
const val MY_AVATAR =
    "https://zulip-avatars.s3.amazonaws.com/39154/76c4ab526496e0b37b446c6df4487bd41d499082?version=2"
const val MY_NAME = "Анохин Александр"

@SuppressLint("SimpleDateFormat")
fun parse(time: Int): String = SimpleDateFormat("d MMM").format(Date(time * 1000L))

internal val emojiList by lazy {
    val result = mutableListOf<Int>()

    for (i in 0x1F601..0x1F64F)
        result.add(i)

    for (i in 0x2702..0x27B0)
        result.add(i)

    result
}