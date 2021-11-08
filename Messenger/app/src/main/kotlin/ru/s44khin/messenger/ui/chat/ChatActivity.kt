package ru.s44khin.messenger.ui.chat

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.Message
import ru.s44khin.messenger.databinding.ActivityChatBinding
import ru.s44khin.messenger.utils.MY_ID
import ru.s44khin.messenger.utils.parse

class ChatActivity : AppCompatActivity() {

    companion object {
        const val STREAM_ID = "streamId"
        const val STREAM_NAME = "streamName"
        const val TOPIC_NAME = "topicName"

        fun createIntent(context: Context, streamId: Int, streamName: String, topicName: String) =
            Intent(context, ChatActivity::class.java)
                .putExtra(STREAM_ID, streamId)
                .putExtra(STREAM_NAME, streamName)
                .putExtra(TOPIC_NAME, topicName)
    }

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private val viewModel: ChatViewModel by viewModels()

    private val streamName by lazy {
        intent.getStringExtra(STREAM_NAME)
    }

    private val streamId by lazy {
        intent.getIntExtra(STREAM_ID, 0)
    }
    private val topicName by lazy {
        intent.getStringExtra(TOPIC_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = ResourcesCompat.getColor(resources, R.color.background, null)
        setContentView(binding.root)
        initToolbar()
        initButtons()
        loadMessages()
    }

    private fun initToolbar() {
        binding.streamName.text = streamName
        binding.topicName.text = topicName
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadMessages() {
        viewModel.getMessages(streamId, topicName ?: "Topic")

        viewModel.messages.observe(this) { messages ->
            if (binding.progressBar.isVisible) {
                initRecyclerView(messages)
                binding.progressBar.visibility = View.GONE
            } else {
                binding.recyclerView.apply {
                    adapter?.notifyItemChanged(viewModel.messages.value!!.lastIndex)
                    scrollToPosition(viewModel.messages.value!!.lastIndex)
                }
            }
        }
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
        if (length == 0) R.drawable.ic_attach else R.drawable.ic_send,
        null
    )

    private fun setButtonsColor(length: Int) = if (length == 0) Color.GRAY else Color.WHITE

    private fun addMessage(text: CharSequence?) {
        if (text?.length != 0) {
            binding.messageInput.message.setText("")
            viewModel.sendMessage(streamName!!, topicName!!, text.toString())
        }
    }
}