package ru.s44khin.coursework.ui.chat

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Message
import ru.s44khin.coursework.databinding.ActivityChatBinding
import ru.s44khin.coursework.utils.parse
import java.util.*

class ChatActivity : AppCompatActivity() {

    companion object {
        const val STREAM = "stream"
        const val TOPIC = "topic"

        fun createIntent(context: Context, stream: String, topic: String) =
            Intent(context, ChatActivity::class.java)
                .putExtra(STREAM, stream)
                .putExtra(TOPIC, topic)
    }

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private var list = mutableListOf<ChatItem>()
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initButtons()
        loadMessages()
    }

    private fun initToolbar() {
        binding.streamName.text = intent.getStringExtra(STREAM)
        binding.topicName.text = intent.getStringExtra(TOPIC)
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadMessages() = viewModel.messages.observe(this) { messages ->
        val result = mutableListOf(
            ChatItem.DateItem(messages[0].date),
            ChatItem.MessageItem(messages[0])
        )

        for (i in 0 until messages.lastIndex) {
            if (messages[i].date != messages[i + 1].date)
                result.add(ChatItem.DateItem(messages[i + 1].date))

            result.add(ChatItem.MessageItem(messages[i + 1]))
        }

        list = result
        initRecyclerView(result)
        binding.progressBar.visibility = View.GONE
    }

    private fun initRecyclerView(list: List<ChatItem>) = binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(list)
        scrollToPosition(list.lastIndex)
    }

    private fun initButtons() = binding.messageInput.message.doAfterTextChanged { text ->
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

            if (parse(message.date) != parse((list[list.lastIndex] as ChatItem.MessageItem).message.date))
                list.add(ChatItem.DateItem(message.date))

            list.add(ChatItem.MessageItem(message))
            binding.messageInput.message.setText("")
            binding.recyclerView.adapter?.notifyItemInserted(list.lastIndex)
            binding.recyclerView.scrollToPosition(list.lastIndex)
        }
    }
}