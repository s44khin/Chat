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
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.ActivityChatBinding

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

    private val viewModel: ChatViewModel by viewModels()

    private val streamName by lazy {
        intent.getStringExtra(STREAM_NAME)!!
    }

    private val streamId by lazy {
        intent.getIntExtra(STREAM_ID, 0)
    }

    private val topicName by lazy {
        intent.getStringExtra(TOPIC_NAME)!!
    }

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolBar()
        initMessages()
        initButtons()
    }

    private fun initToolBar() = binding.apply {
        streamName.text = this@ChatActivity.streamName
        topicName.text = this@ChatActivity.topicName
        backButton.setOnClickListener { finish() }
    }

    private fun initMessages() = viewModel.apply {
        getOldMessages(topicName)
        getMessages(streamId, topicName)

        val layoutManager =
            LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = false
        binding.recyclerView.layoutManager = layoutManager

        oldMessages.observe(this@ChatActivity) {
            binding.recyclerView.apply {
                adapter = ChatAdapter(it, viewModel)
                binding.progressBar.visibility = View.GONE
            }
        }

        var check = false
        messages.observe(this@ChatActivity) {
            binding.recyclerView.apply {
                if (check) {
                    adapter?.notifyItemInserted(it.lastIndex)
                    scrollToPosition(it.lastIndex)
                } else {
                    adapter = ChatAdapter(it, viewModel)
                    binding.progressBar.visibility = View.GONE
                    binding.progressIndicator.visibility = View.GONE
                    check = true
                }
            }
        }
    }

    private fun initButtons() = binding.messageInput.message.doAfterTextChanged { text ->
        binding.messageInput.send.apply {
            setOnClickListener {
                viewModel.sendMessage(streamName, topicName, text.toString())
                binding.messageInput.message.setText("")
            }
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
}