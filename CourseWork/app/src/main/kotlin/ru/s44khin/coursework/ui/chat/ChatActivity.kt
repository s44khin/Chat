package ru.s44khin.coursework.ui.chat

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Message
import ru.s44khin.coursework.data.repository.MainRepository
import ru.s44khin.coursework.databinding.ActivityChatBinding
import ru.s44khin.coursework.ui.adapters.ChatAdapter
import ru.s44khin.coursework.utils.parse
import java.util.*

class ChatActivity : AppCompatActivity() {

    companion object{
        fun createIntent(context: Context) = Intent(context, ChatActivity::class.java)
    }

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private val list: MutableList<Any> by lazy {
        val messages = MainRepository().getMessages()
        val result = mutableListOf(messages[0].date, messages[0])

        for (i in 0 until messages.lastIndex) {
            if (messages[i].date != messages[i + 1].date)
                result.add(messages[i + 1].date)

            result.add(messages[i + 1])
        }

        result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setRecyclerView()
        setButtons()
    }

    private fun setRecyclerView() = binding.recyclerView.apply {
        val manager =
            LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        manager.stackFromEnd = true

        layoutManager = manager
        adapter = ChatAdapter(list)
    }

    private fun setButtons() = binding.messageInput.message.doOnTextChanged { text, _, _, _ ->
        binding.messageInput.send.apply {
            setOnClickListener { addMessage(text) }

            if (text?.length == 0) {
                backgroundTintList = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.message,
                        null
                    )
                )
                setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_attach_file,
                        null
                    )
                )
                setColorFilter(Color.GRAY)
            } else {
                backgroundTintList = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.primary,
                        null
                    )
                )
                setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_send,
                        null
                    )
                )
                setColorFilter(Color.WHITE)
            }
        }
    }

    private fun addMessage(text: CharSequence?) {
        if (text?.length != 0) {
            val message = Message(
                date = (System.currentTimeMillis() / 1000).toInt(),
                avatar = "",
                profile = "Petya Petkin",
                message = text.toString(),
                reactions = mutableListOf(),
                alignment = 1
            )

            if (parse(message.date) != parse((list[list.lastIndex] as Message).date))
                list.add(message.date)

            list.add(message)
            binding.messageInput.message.setText("")
            binding.recyclerView.adapter?.notifyItemInserted(list.lastIndex)
            binding.recyclerView.scrollToPosition(list.lastIndex)
        }
    }
}