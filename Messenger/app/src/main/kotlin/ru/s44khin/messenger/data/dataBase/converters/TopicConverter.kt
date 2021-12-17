package ru.s44khin.messenger.data.dataBase.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import ru.s44khin.messenger.data.model.Topic

class TopicConverter {

    companion object {
        private const val SPLITTER = "?#$%?"
        private val moshi: Moshi = Moshi.Builder().build()
        private val jsonAdapter: JsonAdapter<Topic> = moshi.adapter(Topic::class.java)
    }

    @TypeConverter
    fun fromTopics(topics: List<Topic>): String {
        var result = ""

        for (i in topics.indices) {
            result += jsonAdapter.toJson(topics[i])
            if (i != topics.lastIndex)
                result += SPLITTER
        }

        return result
    }

    @TypeConverter
    fun toTopics(data: String): List<Topic> {
        if (data.isEmpty())
            return emptyList()

        val result = mutableListOf<Topic>()
        val arr = data.split(SPLITTER)

        for (topicStr in arr)
            result.add(jsonAdapter.fromJson(topicStr)!!)

        return result
    }
}