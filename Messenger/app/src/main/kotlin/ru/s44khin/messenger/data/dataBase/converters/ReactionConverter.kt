package ru.s44khin.messenger.data.dataBase.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import ru.s44khin.messenger.data.model.AdapterReaction

class ReactionConverter {

    companion object {
        private const val SPLITTER = "/#/"
        private val moshi: Moshi = Moshi.Builder().build()
        private val jsonAdapter: JsonAdapter<AdapterReaction> =
            moshi.adapter(AdapterReaction::class.java)
    }

    @TypeConverter
    fun fromReactions(reactions: List<AdapterReaction>): String {
        var result = ""

        for (i in reactions.indices) {
            result += jsonAdapter.toJson(reactions[i])
            if (i != reactions.lastIndex)
                result += SPLITTER
        }

        return result
    }

    @TypeConverter
    fun toReactions(data: String): List<AdapterReaction> {
        if (data.isEmpty())
            return emptyList()

        val result = mutableListOf<AdapterReaction>()
        val arr = data.split(SPLITTER)

        for (reactionStr in arr)
            result.add(jsonAdapter.fromJson(reactionStr)!!)

        return result
    }
}