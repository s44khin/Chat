package ru.s44khin.coursework

import android.app.Application
import android.util.Log
import io.reactivex.plugins.RxJavaPlugins

class MessengerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable ->
            throwable.message?.let { Log.e("ERROR", it) }
        }
    }
}