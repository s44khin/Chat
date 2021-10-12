package ru.s44khin.coursework.ui.chat

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.messageInput.message.doOnTextChanged { text, _, _, _ ->
            binding.messageInput.send.apply {
                when (text?.length ?: 0) {
                    0 -> {
                        setImageResource(R.drawable.round_attach_file_24)
                        backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.message, null))
                        setColorFilter(Color.GRAY)
                    }
                    else -> {
                        setImageResource(R.drawable.round_send_24)
                        backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.primary, null))
                        setColorFilter(Color.WHITE)
                    }
                }
            }
        }
    }
}