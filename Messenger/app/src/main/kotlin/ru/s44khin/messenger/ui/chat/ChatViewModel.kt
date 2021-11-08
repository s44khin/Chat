package ru.s44khin.messenger.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.data.model.AdapterMessage
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.model.Reaction
import ru.s44khin.messenger.data.repository.MainRepository
import ru.s44khin.messenger.utils.MY_AVATAR
import ru.s44khin.messenger.utils.MY_ID
import ru.s44khin.messenger.utils.MY_NAME
import ru.s44khin.messenger.utils.parse

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<MutableList<ChatItem>>()
    val messages: LiveData<MutableList<ChatItem>> = _messages

    private val disposeBag = CompositeDisposable()
    private val repository = MainRepository()

    fun getMessages(streamId: Int, topicName: String) = repository.getMessages(streamId, topicName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                val newMessages = it.messages
                val result = mutableListOf(
                    ChatItem.DateItem(parse(newMessages[0].timestamp)),
                    ChatItem.MessageItem(newMessages[0].toAdapterMessage())
                )
                for (i in 0 until newMessages.lastIndex) {
                    if (parse(newMessages[i].timestamp) != parse(newMessages[i + 1].timestamp))
                        result.add(ChatItem.DateItem(parse(newMessages[i + 1].timestamp)))

                    result.add(ChatItem.MessageItem(newMessages[i + 1].toAdapterMessage()))
                }

                _messages.value = result
            },
            onError = { Log.e("Error", it.message.toString()) }
        )

    fun sendMessage(
        streamName: String,
        topicName: String,
        content: String
    ) = repository.sendMessage(streamName, topicName, content)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                val result = _messages.value!!
                val lastMessage = result[result.lastIndex] as ChatItem.MessageItem
                val currentTime = parse((System.currentTimeMillis() / 1000).toInt())

                if (currentTime != lastMessage.message.time)
                    result.add(ChatItem.DateItem(currentTime))

                result.add(
                    ChatItem.MessageItem(
                        AdapterMessage(
                            time = currentTime,
                            avatar = MY_AVATAR,
                            profile = MY_NAME,
                            content = content,
                            isMyMessage = true,
                            reactions = mutableListOf()
                        )
                    )
                )

                _messages.value = result
            },
            onError = { Log.e("Error", it.message.toString()) }
        )

    private fun Message.toAdapterMessage() = AdapterMessage(
        time = parse(this.timestamp),
        avatar = this.avatar,
        profile = this.profile,
        content = this.content,
        isMyMessage = this.senderId == MY_ID,
        reactions = this.reactions.toAdapterReactions()
    )

    private fun List<Reaction>.toAdapterReactions(): MutableList<AdapterReaction> {
        val result = mutableListOf<AdapterReaction>()
        val map = mutableMapOf<String, Int>()
        val reactionsILiked = mutableListOf<String>()

        for (reaction in this) {
            if (reaction.emojiCode in map.keys)
                map[reaction.emojiCode] = map[reaction.emojiCode]!! + 1
            else
                map[reaction.emojiCode] = 1

            if (reaction.user.id == MY_ID)
                reactionsILiked.add(reaction.emojiCode)
        }

        for ((emoji, count) in map)
            result.add(
                AdapterReaction(
                    emoji = emoji,
                    count = count,
                    iLiked = emoji in reactionsILiked
                )
            )

        return result
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }
}