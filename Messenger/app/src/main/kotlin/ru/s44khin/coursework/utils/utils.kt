package ru.s44khin.coursework.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun parse(time: Int): String = SimpleDateFormat("d MMM").format(Date(time * 1000L))

internal val emojiList by lazy {
    val result = mutableListOf<String>()

    for (i in 0x1F601..0x1F64F)
        result.add(String(Character.toChars(i)))

    for (i in 0x2702..0x27B0)
        result.add(String(Character.toChars(i)))

    result
}