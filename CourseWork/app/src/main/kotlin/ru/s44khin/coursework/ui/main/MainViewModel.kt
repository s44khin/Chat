package ru.s44khin.coursework.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.coursework.data.model.Profile
import ru.s44khin.coursework.data.model.Stream
import ru.s44khin.coursework.data.repository.MainRepository

class MainViewModel : ViewModel() {

    private var _subsStreams = MutableLiveData<List<Stream>>()
    val subsStreams: LiveData<List<Stream>> = _subsStreams

    private var _allStreams = MutableLiveData<List<Stream>>()
    val allStreams: LiveData<List<Stream>> = _allStreams

    private val _people = MutableLiveData<List<Profile>>()
    val people: LiveData<List<Profile>> = _people

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val disposeBag = CompositeDisposable()

    init {
        downloadPeople()
        downloadProfile()
        downloadAllStreams()
        downloadSubsStreams()
    }

    private fun downloadPeople() {
        MainRepository().getPeople()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { _people.value = it },
                onError = { Log.e("Error", it.message.toString()) }
            )
            .addTo(disposeBag)
    }

    private fun downloadProfile() {
        MainRepository().getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { _profile.value = it },
                onError = { Log.e("Error", it.message.toString()) }
            )
            .addTo(disposeBag)
    }

    private fun downloadSubsStreams() {
        MainRepository().getSubsStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { _subsStreams.value = it },
                onError = { Log.e("Error", it.message.toString()) }
            )
            .addTo(disposeBag)
    }

    private fun downloadAllStreams() {
        MainRepository().getAllStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { _allStreams.value = it },
                onError = { Log.e("Error", it.message.toString()) }
            )
            .addTo(disposeBag)
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }
}