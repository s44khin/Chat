package ru.s44khin.messenger.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.repository.MainRepository

class MainViewModel : ViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _members = MutableLiveData<List<Profile>>()
    val members: LiveData<List<Profile>> = _members

    private val _subsStreams = MutableLiveData<List<ResultStream>>()
    val subsStreams: LiveData<List<ResultStream>> = _subsStreams

    private val _allStreams = MutableLiveData<List<ResultStream>>()
    val allStreams: LiveData<List<ResultStream>> = _allStreams

    private val disposeBag = CompositeDisposable()
    private val repository = MainRepository()

    init {
        getSubsStreams()
        getAllStreams()
        getMembers()
        getSelfProfile()
    }

    private fun getAllStreams() = repository.getAllStreams()
        .flattenAsObservable() { it.streams }
        .flatMap {
            Observable.zip(
                Observable.just(it),
                repository.getTopics(it.streamId).subscribeOn(Schedulers.io()).toObservable()
            ) { stream, topics ->
                ResultStream(
                    name = stream.name,
                    description = stream.description,
                    streamId = stream.streamId,
                    topics = topics.topics
                )
            }
        }
        .toList()
        .subscribeBy(
            onSuccess = { _allStreams.postValue(it) },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    private fun getSubsStreams() = repository.getSubsStreams()
        .flattenAsObservable() { it.subscriptions }
        .flatMap {
            Observable.zip(
                Observable.just(it),
                repository.getTopics(it.streamId).subscribeOn(Schedulers.io()).toObservable()
            ) { stream, topics ->
                ResultStream(
                    name = stream.name,
                    description = stream.description,
                    streamId = stream.streamId,
                    topics = topics.topics
                )
            }
        }
        .toList()
        .subscribeBy(
            onSuccess = { _subsStreams.postValue(it) },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    private fun getMembers() = repository.getMembers()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { _members.value = it.members },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    private fun getSelfProfile() = repository.getSelfProfile()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { _profile.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }
}