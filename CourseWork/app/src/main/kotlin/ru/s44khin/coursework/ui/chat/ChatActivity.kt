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

    companion object {
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
        setSupportActionBar(binding.toolBar)
        initRecyclerView()
        initButtons()
    }

    private fun initRecyclerView() = binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(list)
        scrollToPosition(list.lastIndex)
    }

    private fun initButtons() = binding.messageInput.message.doOnTextChanged { text, _, _, _ ->
        binding.messageInput.send.apply {
            setOnClickListener { addMessage(text) }
            backgroundTintList = setButtonsBackground(text?.length ?: 0)
            setImageDrawable(setButtonsImage(text?.length ?: 0))
            setColorFilter(setButtonsColor(text?.length ?: 0))
        }
    }

    private fun setButtonsBackground(length: Int) = ColorStateList.valueOf(
        ResourcesCompat.getColor(
            resources,
            if (length == 0) R.color.message else R.color.primary,
            null
        )
    )

    private fun setButtonsImage(length: Int) = ResourcesCompat.getDrawable(
        resources,
        if (length == 0) R.drawable.ic_attach_file else R.drawable.ic_send,
        null
    )

    private fun setButtonsColor(length: Int) = if (length == 0) Color.GRAY else Color.WHITE

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