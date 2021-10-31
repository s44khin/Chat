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
import java.util.concurrent.TimeUnit

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
    private val repository = MainRepository()

    init {
        downloadPeople()
        downloadProfile()
        searchSubsStreams()
        searchAllStreams()
    }

    fun searchSubsStreams(text: String = "") = repository.getSubsStreams(text)
        .subscribeOn(Schedulers.io())
        .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = {
                _subsStreams.value = it
            },
            onError = {
                Log.e("Error", it.message.toString())
            }
        )
        .addTo(disposeBag)

    fun searchAllStreams(text: String = "") = repository.getAllStreams(text)
        .subscribeOn(Schedulers.io())
        .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = {
                _allStreams.value = it
            },
            onError = {
                Log.e("Error", it.message.toString())
            }
        )
        .addTo(disposeBag)

    private fun downloadPeople() = repository.getPeople()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { _people.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    private fun downloadProfile() = repository.getProfile()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { _profile.value = it },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }
}