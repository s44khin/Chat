package ru.s44khin.messenger.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.data.model.Reaction
import ru.s44khin.messenger.data.repository.MainRepository
import ru.s44khin.messenger.utils.MY_ID
import ru.s44khin.messenger.utils.parse

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<ChatItem>>()
    val messages: LiveData<List<ChatItem>> = _messages

    private val disposeBag = CompositeDisposable()
    private val repository = MainRepository()

    fun getMessages(streamId: Int, topicName: String) = repository.getMessages(streamId, topicName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { baseMessages ->
                val messages = baseMessages.messages
                val result = mutableListOf(
                    ChatItem.Date(parse(messages[0].timestamp)),
                    messages[0].toMessageItem()
                )

                for (i in 0 until messages.lastIndex) {
                    if (parse(messages[i].timestamp) != parse(messages[i + 1].timestamp))
                        result.add(ChatItem.Date(parse(messages[i + 1].timestamp)))

                    result.add(messages[i + 1].toMessageItem())
                }

                _messages.value = result
            },
            onError = { }
        )
        .addTo(disposeBag)

    fun addReaction(messageId: Int, emojiName: String) = repository.addReaction(messageId, emojiName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {  },
            onError = {  }
        )

    fun deleteReaction(messageId: Int, emojiName: String) = repository.deleteReaction(messageId, emojiName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {  },
            onError = {  }
        )

    private fun List<Reaction>.toAdapterReactions(): MutableList<AdapterReaction> {
        val result = mutableListOf<AdapterReaction>()
        val map = mutableMapOf<Pair<String, String>, Int>()
        val reactionsILiked = mutableListOf<String>()

        for (reaction in this) {
            if (reaction.emojiCode to reaction.emojiName in map.keys)
                map[reaction.emojiCode to reaction.emojiName]?.plus(1)
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

    private fun Message.toMessageItem() = ChatItem.Message(
        id = this.id,
        time = parse(this.timestamp),
        avatar = this.avatar,
        profile = this.profile,
        content = this.content,
        reactions = this.reactions.toAdapterReactions(),
        isMyMessage = MY_ID == this.senderId
    )
}