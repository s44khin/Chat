package ru.s44khin.messenger.data.network

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.Credentials
import okhttp3.OkHttpClient
import ru.s44khin.messenger.data.network.api.API_KEY
import ru.s44khin.messenger.data.network.api.EMAIL
import java.io.InputStream

@GlideModule
class CustomGlideModule: AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.authenticator { _, response ->
            val credentials = Credentials.basic(EMAIL, API_KEY)
            response.request.newBuilder().header("Authorization", credentials).build()
        }

        val factory = OkHttpUrlLoader.Factory(okHttpClient.build())
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}