package ru.s44khin.messenger.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.model.Reaction
import ru.s44khin.messenger.data.repository.MainRepository
import ru.s44khin.messenger.utils.MY_AVATAR
import ru.s44khin.messenger.utils.MY_ID
import ru.s44khin.messenger.utils.MY_NAME
import ru.s44khin.messenger.utils.parse

class ChatViewModel : ViewModel() {

    private val _oldMessages = MutableLiveData<MutableList<ChatItem>>()
    val oldMessages: LiveData<MutableList<ChatItem>> = _oldMessages

    private val _messages = MutableLiveData<MutableList<ChatItem>>()
    val messages: LiveData<MutableList<ChatItem>> = _messages

    private val disposeBag = CompositeDisposable()
    private val repository = MainRepository()
    private val dataBase = MessengerApplication.instance.dataBase

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getOldMessages(topicName: String) = dataBase.messagesDao().getAll(topicName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                if (it != null && it.isNotEmpty()) {
                    val result = mutableListOf(
                        ChatItem.Date(it[0].time),
                        it[0]
                    )

                    for (i in 0 until it.lastIndex) {
                        if (it[i].time != it[i + 1].time)
                            result.add(ChatItem.Date(it[i + 1].time))

                        result.add(it[i + 1])
                    }

                    _oldMessages.value = result
                }
            },
            onError = { Log.e("Error", it.message.toString()) }
        )

    fun getMessages(streamId: Int, topicName: String) = repository.getMessages(streamId, topicName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { baseMessages ->
                val messages = baseMessages.messages
                val onlyMessages = mutableListOf(
                    messages[0].toMessageItem(topicName)
                )
                val result = mutableListOf(
                    ChatItem.Date(parse(messages[0].timestamp)),
                    messages[0].toMessageItem(topicName)
                )

                for (i in 0 until messages.lastIndex) {
                    if (parse(messages[i].timestamp) != parse(messages[i + 1].timestamp))
                        result.add(ChatItem.Date(parse(messages[i + 1].timestamp)))

                    val temp = messages[i + 1].toMessageItem(topicName)
                    result.add(temp)
                    onlyMessages.add(temp)
                }

                _messages.value = result

                Single.fromCallable { dataBase.messagesDao().insertAll(onlyMessages) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onError = { Log.e("Error", it.message.toString()) }
                    )
                    .addTo(disposeBag)
            },
            onError = {
                Log.e("Error", it.message.toString())
                _error.postValue(it)
            }
        )
        .addTo(disposeBag)

    fun sendMessage(streamName: String, topicName: String, content: String) =
        repository.sendMessage(streamName, topicName, content)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val newMessages = _messages.value

                    if (!newMessages.isNullOrEmpty()) {
                        val time = parse((System.currentTimeMillis() / 1000).toInt())

                        if (time != (newMessages.last() as ChatItem.Message).time)
                            newMessages.add(ChatItem.Date(time))

                        newMessages.add(
                            ChatItem.Message(
                                id = MY_ID,
                                topicName = topicName,
                                time = time,
                                avatar = MY_AVATAR,
                                profile = MY_NAME,
                                content = content,
                                isMyMessage = true,
                                reactions = mutableListOf()
                            )
                        )
                    }

                    _messages.value = newMessages!!
                },
                onError = {
                    Log.e("Error", it.message.toString())
                    _error.postValue(it)
                }
            )

    fun addReaction(messageId: Int, emojiName: String) =
        repository.addReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { },
                onError = { Log.e("Error", it.message.toString()) }
            )

    fun deleteReaction(messageId: Int, emojiName: String) =
        repository.deleteReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { },
                onError = { Log.e("Error", it.message.toString()) }
            )

    private fun List<Reaction>.toAdapterReactions(): MutableList<AdapterReaction> {
        val result = mutableListOf<AdapterReaction>()
        val map = mutableMapOf<Pair<String, String>, Int>()
        val reactionsILiked = mutableListOf<String>()

        for (reaction in this) {
            if (reaction.emojiCode to reaction.emojiName in map.keys)
                map[reaction.emojiCode to reaction.emojiName] =
                    map[reaction.emojiCode to reaction.emojiName]!! + 1
            else
                map[reaction.emojiCode to reaction.emojiName] = 1

            if (reaction.user.id == MY_ID)
                reactionsILiked.add(reaction.emojiCode)
        }

        for ((emoji, count) in map)
            result.add(
                AdapterReaction(
                    emojiCode = emoji.first,
                    emojiName = emoji.second,
                    count = count,
                    iLiked = emoji.first in reactionsILiked
                )
            )

        return result
    }

    private fun Message.toMessageItem(topicName: String) = ChatItem.Message(
        id = this.id,
        topicName = topicName,
        time = parse(this.timestamp),
        avatar = this.avatar,
        profile = this.profile,
        content = this.content,
        reactions = this.reactions.toAdapterReactions(),
        isMyMessage = MY_ID == this.senderId
    )
}