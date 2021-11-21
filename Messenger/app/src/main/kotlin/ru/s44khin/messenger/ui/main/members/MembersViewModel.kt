package ru.s44khin.messenger.ui.main.members

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
import ru.s44khin.messenger.data.model.Profile

class MembersViewModel : ViewModel() {

    private val _oldMembers = MutableLiveData<List<Profile>>()
    val oldMembers: LiveData<List<Profile>> = _oldMembers
    private val _newMembers = MutableLiveData<List<Profile>>()
    val newMembers: LiveData<List<Profile>> = _newMembers

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error


    private val disposeBag = CompositeDisposable()
    private val repository = MessengerApplication.instance.repository
    private val dataBase = MessengerApplication.instance.dataBase

    fun getOldMembers() = dataBase.profileDao().getAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                if (it != null)
                    _oldMembers.value = it
            },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun getNewMembers() = repository.getMembers()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = { newMembers ->
                _newMembers.value = newMembers.members
                Single.fromCallable { dataBase.profileDao().insertAll(newMembers.members) }
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

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }
}