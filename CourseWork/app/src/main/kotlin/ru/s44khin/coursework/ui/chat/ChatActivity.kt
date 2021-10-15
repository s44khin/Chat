package ru.s44khin.coursework.ui.chat

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

class ChatActivity : AppCompatActivity() {

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private val messages: MutableList<Message> by lazy {
        MainRepository().getMessages().toMutableList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setButtons()

        binding.recyclerView.apply {
            val manager =
                LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
            manager.stackFromEnd = true

            layoutManager = manager
            adapter = ChatAdapter(messages)
        }
    }

    private fun setButtons() = binding.messageInput.message.doOnTextChanged { text, _, _, _ ->
        binding.messageInput.send.apply {
            when (text?.length ?: 0) {
                0 -> {
                    setImageResource(R.drawable.round_attach_file_24)
                    backgroundTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.chatBottomInput,
                            null
                        )
                    )
                    setColorFilter(Color.GRAY)
                }
                else -> {
                    setImageResource(R.drawable.round_send_24)
                    backgroundTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.primary,
                            null
                        )
                    )
                    setColorFilter(Color.WHITE)
                }
            }
        }

        binding.messageInput.send.setOnClickListener {
            val message = binding.messageInput.message.text.toString()

            if (message.isNotEmpty()) {
                messages.add(
                    Message(
                        avatar = "https://offvkontakte.ru/wp-content/uploads/avatarka-pustaya-vk_20.jpg",
                        profile = "Ivan Ivanov",
                        message = message,
                        alignment = 1,
                        reactions = listOf()
                    )
                )

                binding.messageInput.message.setText("")
                binding.recyclerView.adapter?.notifyItemInserted(messages.size)
            }
        }
    }
}