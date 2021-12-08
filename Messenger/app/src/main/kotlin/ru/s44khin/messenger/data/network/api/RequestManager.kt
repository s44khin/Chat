package ru.s44khin.messenger.data.network.api

import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.s44khin.messenger.data.network.ZulipService

object RequestManager {

    private const val MAIN_SERVER = "https://tinkoff-android-fall21.zulipchat.com/"

    private val retrofitClient: Retrofit.Builder by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.authenticator { _, response ->
            val credentials = Credentials.basic(EMAIL, API_KEY)
            response.request.newBuilder().header("Authorization", credentials).build()
        }
        okHttpClient.addInterceptor(httpLoggingInterceptor)

        Retrofit.Builder()
            .baseUrl(MAIN_SERVER)
            .client(okHttpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create())
    }

    val service: ZulipService by lazy {
        retrofitClient
            .build()
            .create(ZulipService::class.java)
    }
}