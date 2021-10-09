package ru.s44khin.coursework.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.ActivityMainBinding
import ru.s44khin.coursework.ui.views.EmojiView
import ru.s44khin.coursework.ui.views.FlexBoxLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val rand = Random(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.messageFirst.flexBoxLayout.addView(
            ImageView(this).apply {
                background =
                    ResourcesCompat.getDrawable(resources, R.drawable.emoji_view_background, null)
                setImageResource(R.drawable.outline_add_24)
                setPadding(13)
                setOnClickListener { addOnCLick(binding.messageFirst.flexBoxLayout) }
            }
        )

        binding.messageSecond.flexBoxLayout.addView(
            ImageView(this).apply {
                background =
                    ResourcesCompat.getDrawable(resources, R.drawable.emoji_view_background, null)
                setImageResource(R.drawable.outline_add_24)
                setPadding(13)
                setOnClickListener { addOnCLick(binding.messageSecond.flexBoxLayout) }
            }
        )
    }

    private fun addOnCLick(flexBoxLayout: FlexBoxLayout) {
        flexBoxLayout.addView(
            EmojiView(this).apply {
                setPadding(20)
                emoji = getRandomEmoji(rand)
                text = (0..1004).random(rand).toString()
                textColor = Color.BLACK
                background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.emoji_view_background,
                    null
                )
                setOnClickListener { emojiView ->
                    emojiView as EmojiView
                    val value = Integer.parseInt(emojiView.text)

                    if (emojiView.isSelected)
                        emojiView.text = (value - 1).toString()
                    else
                        emojiView.text = (value + 1).toString()

                    emojiView.isSelected = !emojiView.isSelected
                }
            }
        )
    }

    private fun getRandomEmoji(rand: Random) = when ((0..9).random(rand)) {
        0 -> "❤️"
        1 -> "🤣"
        2 -> "😊"
        3 -> "😍"
        4 -> "💕"
        5 -> "👍"
        6 -> "🤦‍♂️"
        7 -> "😎"
        8 -> "😢"
        9 -> "🤔"
        else -> "😴"
    }
}