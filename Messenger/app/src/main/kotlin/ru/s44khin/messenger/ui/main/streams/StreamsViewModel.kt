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
import ru.s44khin.messenger.data.model.*

class StreamsViewModel : ViewModel() {

    private val _oldAllStreams = MutableLiveData<List<ResultStream>>()
    val oldAllStreams: LiveData<List<ResultStream>> = _oldAllStreams
    private val _newAllStreams = MutableLiveData<List<ResultStream>>()
    val newAllStreams: LiveData<List<ResultStream>> = _newAllStreams

    private val _oldSubsStreams = MutableLiveData<List<ResultStream>>()
    val oldSubsStreams: LiveData<List<ResultStream>> = _oldSubsStreams
    private val _newSubsStreams = MutableLiveData<List<ResultStream>>()
    val newSubsStreams: LiveData<List<ResultStream>> = _newSubsStreams

    private val _searchSubsStreams = MutableLiveData<List<ResultStream>>()
    val searchSubsStreams: LiveData<List<ResultStream>> = _searchSubsStreams
    private val _searchAllStreams = MutableLiveData<List<ResultStream>>()
    val searchAllStreams: LiveData<List<ResultStream>> = _searchAllStreams

    private val disposeBag = CompositeDisposable()
    private val repository = MessengerApplication.instance.repository
    private val dataBase = MessengerApplication.instance.dataBase

    fun getOldAllStreams() = dataBase.allStreamsDao().getAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                if (it != null)
                    _oldAllStreams.value = it.toResultStreamList()
                else
                    _oldAllStreams.value = emptyList()
            },
            onError = { Log.e("Error", it.message.toString()) }
        )

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
                _newAllStreams.postValue(it)
                Single.fromCallable { dataBase.allStreamsDao().insertAll(it.toAllStreamList()) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onError = { Log.e("Error", it.message.toString()) }
                    )
                    .addTo(disposeBag)
            },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun getOldSubsStreams() = dataBase.subsStreamsDao().getAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                if (it != null) {
                    _oldSubsStreams.value = it.toResultStreamList()
                }
                else
                    _oldSubsStreams.value = emptyList()
            },
            onError = { Log.e("Error", it.message.toString()) }
        )

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
            onSuccess = {
                _newSubsStreams.postValue(it)
                Single.fromCallable { dataBase.subsStreamsDao().insertAll(it.toSubsStreamList()) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onError = { Log.e("Error", it.message.toString()) }
                    )
                    .addTo(disposeBag)
            },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun searchSubsStreams(text: String) = Observable.fromCallable { _newSubsStreams.value }
        .map { streams -> streams.filter { it.name.contains(text, true) } }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = { _searchSubsStreams.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun searchAllStreams(text: String) = Observable.fromCallable { _newAllStreams.value }
        .map { streams -> streams.filter { it.name.contains(text, true) } }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = { _searchAllStreams.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    private fun List<AllStream>.toResultStreamList(): List<ResultStream> {
        val result = mutableListOf<ResultStream>()

        for (stream in this)
            result.add(
                ResultStream(
                    streamId = stream.streamId,
                    description = stream.description,
                    name = stream.name,
                    topics = emptyList()
                )
            )

        return result
    }

    @JvmName("toResultStreamListSubsStream")
    private fun List<SubsStream>.toResultStreamList(): List<ResultStream> {
        val result = mutableListOf<ResultStream>()

        for (stream in this)
            result.add(
                ResultStream(
                    streamId = stream.streamId,
                    description = stream.description,
                    name = stream.name,
                    topics = emptyList()
                )
            )

        return result
    }

    private fun List<ResultStream>.toAllStreamList(): List<AllStream> {
        val result = mutableListOf<AllStream>()

        for (stream in this)
            result.add(
                AllStream(
                    streamId = stream.streamId,
                    description = stream.description,
                    name = stream.name
                )
            )

        return result
    }

    private fun List<ResultStream>.toSubsStreamList(): List<SubsStream> {
        val result = mutableListOf<SubsStream>()

        for (stream in this)
            result.add(
                SubsStream(
                    streamId = stream.streamId,
                    description = stream.description,
                    name = stream.name
                )
            )

        return result
    }

    private fun resultStreamFromStreamAndTopics(
        stream: Stream,
        topics: List<Topic>
    ) = ResultStream(
        streamId = stream.streamId,
        description = stream.description,
        name = stream.name,
        topics = topics
    )
}