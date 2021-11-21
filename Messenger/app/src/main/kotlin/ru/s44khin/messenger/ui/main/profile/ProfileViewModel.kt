package ru.s44khin.messenger.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.utils.MY_ID

class ProfileViewModel : ViewModel() {

    private val _oldProfile = MutableLiveData<Profile>()
    val oldProfile: LiveData<Profile> = _oldProfile
    private val _newProfile = MutableLiveData<Profile>()
    val newProfile: LiveData<Profile> = _newProfile

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    private val disposeBag = CompositeDisposable()
    private val repository = MessengerApplication.instance.repository
    private val dataBase = MessengerApplication.instance.dataBase

    fun getOldProfile() = dataBase.profileDao().getById(MY_ID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                if (it != null)
                    _oldProfile.value = it
            },
            onError = { Log.e("Error", it.message.toString()) }
        )
        .addTo(disposeBag)

    fun getNewProfile() = repository.getSelfProfile()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                _newProfile.value = it
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