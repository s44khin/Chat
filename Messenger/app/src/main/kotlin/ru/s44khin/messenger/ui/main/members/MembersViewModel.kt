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

    private var _members = MutableLiveData<List<Profile>>()
    val members: LiveData<List<Profile>> = _members

    private val disposeBag = CompositeDisposable()
    private val repository = MessengerApplication.instance.repository
    private val dataBase = MessengerApplication.instance.dataBase

    fun getMembers() {
        dataBase.profileDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if (it != null)
                        _members.value = it
                },
                onError = { Log.e("Error", it.message.toString()) }
            )
            .addTo(disposeBag)

        repository.getMembers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { newMembers ->
                    _members.value = newMembers.members
                    Single.fromCallable { dataBase.profileDao().insertAll(newMembers.members) }
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
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }
}