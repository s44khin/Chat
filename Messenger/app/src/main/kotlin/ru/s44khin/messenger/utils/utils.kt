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

const val MY_ID = 454991
const val MY_AVATAR =
    "https://zulip-avatars.s3.amazonaws.com/39154/76c4ab526496e0b37b446c6df4487bd41d499082?version=2"
const val MY_NAME = "Анохин Александр"

const val MY_EMAIL = "s44khin@gmail.com"

@SuppressLint("SimpleDateFormat")
fun parse(time: Int): String = SimpleDateFormat("d MMM").format(Date(time * 1000L))

@SuppressLint("SimpleDateFormat")
fun parse2(time: Int): String = SimpleDateFormat("dd.MM.yyyy").format(Date(time * 1000L))

fun hexToEmoji(string: String) = String(Character.toChars(string.toInt(16)))

fun resultStreamFromStreamAndTopics(stream: Stream, topics: List<Topic>) = ResultStream(
    streamId = stream.streamId,
    description = stream.description,
    name = stream.name,
    date = stream.date,
    pinToTop = stream.pinToTop,
    color = stream.color,
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