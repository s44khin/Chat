package ru.s44khin.coursework.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.coursework.data.model.Message
import ru.s44khin.coursework.data.repository.MainRepository

class ChatViewModel : ViewModel() {

    private var _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val disposeBag = CompositeDisposable()
    private val repository = MainRepository()

    init {
        downloadMessages()
    }

    private fun downloadMessages() = repository.getMessages()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                _messages.value = it
            },
            onError = {
                Log.e("Error", it.message.toString())
            }
        )
        .addTo(disposeBag)

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }
}