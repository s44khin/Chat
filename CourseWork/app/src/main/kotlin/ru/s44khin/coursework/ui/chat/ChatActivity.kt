package ru.s44khin.coursework.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.s44khin.coursework.data.repository.MainRepository
import ru.s44khin.coursework.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private val list: List<Any> by lazy {
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

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)

            adapter = ChatAdapter(list)
        }
    }
}