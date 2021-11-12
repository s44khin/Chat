package ru.s44khin.messenger.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        intent.getStringExtra(STREAM_NAME)
    }

    private val streamId by lazy {
        intent.getIntExtra(STREAM_ID, 0)
    }
    private val topicName by lazy {
        intent.getStringExtra(TOPIC_NAME)
    }

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolBar()
        initMessages()
    }

    private fun initToolBar() = binding.apply {
        streamName.text = this@ChatActivity.streamName
        topicName.text = this@ChatActivity.topicName
        backButton.setOnClickListener { finish() }
    }

    private fun initMessages() = viewModel.apply {
        getMessages(streamId, topicName ?: "")
        messages.observe(this@ChatActivity) {
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = ChatAdapter(it, viewModel)
            }
            binding.progressBar.visibility = View.GONE
        }
    }
}