package ru.s44khin.messenger.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.model.Stream
import ru.s44khin.messenger.data.model.Topic
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun parse(time: Int): String = SimpleDateFormat("d MMM").format(Date(time * 1000L))

@SuppressLint("SimpleDateFormat")
fun parse2(time: Int): String = SimpleDateFormat("dd.MM.yyyy").format(Date(time * 1000L))

fun hexToEmoji(string: String) = String(Character.toChars(string.toInt(16)))

fun resultStreamFromStreamAndTopics(
    stream: Stream,
    subscription: Boolean,
    topics: List<Topic>
) = ResultStream(
    streamId = stream.streamId,
    description = stream.description,
    name = stream.name,
    date = stream.date,
    pinToTop = stream.pinToTop,
    color = stream.color,
    subscription = subscription,
    topics = topics
)

fun showSnackbar(
    context: Context,
    view: View,
    indicator: View?,
    onUpdate: () -> Unit
) {
    indicator?.visibility = View.GONE

    val snackbar = Snackbar.make(
        view,
        context.getString(R.string.internetError),
        Snackbar.LENGTH_INDEFINITE
    )

    snackbar.setAction("Update") {
        onUpdate()
    }

    val snackbarView = snackbar.view
    snackbarView.translationY = -(58 * context.resources.displayMetrics.density)

    snackbar.show()
}