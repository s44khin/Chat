package ru.s44khin.messenger.ui.main.streams

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.model.Stream
import ru.s44khin.messenger.data.model.Topic

class StreamsViewModel : ViewModel() {

    private val _oldStreams = MutableLiveData<List<ResultStream>>()
    val oldStreams: LiveData<List<ResultStream>> = _oldStreams

    private val _allStreams = MutableLiveData<List<ResultStream>>()
    val allStreams: LiveData<List<ResultStream>> = _allStreams

    private val _subsStreams = MutableLiveData<List<ResultStream>>()
    val subsStreams: LiveData<List<ResultStream>> = _subsStreams

    private val _searchSubsStreams = MutableLiveData<List<ResultStream>>()
    val searchSubsStreams: LiveData<List<ResultStream>> = _searchSubsStreams
    private val _searchAllStreams = MutableLiveData<List<ResultStream>>()
    val searchAllStreams: LiveData<List<ResultStream>> = _searchAllStreams

    private val disposeBag = CompositeDisposable()
    private val repository = MessengerApplication.instance.repository
    private val dataBase = MessengerApplication.instance.dataBase

    fun getStreamsFromDB() = dataBase.streamsDao().getAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { _oldStreams.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun getNewAllStreams() = repository.getAllStreams()
        .flattenAsObservable { it.streams }
        .flatMap {
            Observable.zip(
                Observable.just(it),
                repository.getTopics(it.streamId).subscribeOn(Schedulers.io()).toObservable()
            ) { stream, topics -> resultStreamFromStreamAndTopics(stream, topics.topics) }
        }
        .toList()
        .subscribeBy(
            onSuccess = {
                _allStreams.postValue(it)
                Single.fromCallable { dataBase.streamsDao().insertAll(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onError = { error -> Log.e("Error", error.message.toString()) }
                    )
                    .addTo(disposeBag)
            },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun getNewSubsStreams() = repository.getSubsStreams()
        .flattenAsObservable { it.subscriptions }
        .flatMap {
            Observable.zip(
                Observable.just(it),
                repository.getTopics(it.streamId).subscribeOn(Schedulers.io()).toObservable()
            ) { stream, topics -> resultStreamFromStreamAndTopics(stream, topics.topics) }
        }
        .toList()
        .subscribeBy(
            onSuccess = { _subsStreams.postValue(it) },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun searchSubsStreams(text: String) = Observable.fromCallable { _subsStreams.value }
        .map { streams -> streams.filter { it.name.contains(text, true) } }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = { _searchSubsStreams.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun searchAllStreams(text: String) = Observable.fromCallable { _allStreams.value }
        .map { streams -> streams.filter { it.name.contains(text, true) } }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = { _searchAllStreams.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    private fun resultStreamFromStreamAndTopics(stream: Stream, topics: List<Topic>) = ResultStream(
        streamId = stream.streamId,
        description = stream.description,
        name = stream.name,
        topics = topics
    )
}