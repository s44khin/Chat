package ru.s44khin.coursework.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import ru.s44khin.coursework.R
import ru.s44khin.coursework.databinding.ActivityMainBinding
import ru.s44khin.coursework.ui.views.EmojiView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val rand = Random(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.flexBoxLayout.addView(
            EmojiView(this).apply {
                text = "${(0..2000).random(rand)}"
                emoji = getRandomEmoji(rand)
                setPadding(20)
                marginBetween = 30f
                textSize = 40f
                textColor = Color.WHITE
                background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.emoji_view_background,
                    null
                )
            }
        )

        binding.add.setOnClickListener {
            binding.flexBoxLayout.addView(
                EmojiView(this).apply {
                    text = "${(0..2000).random(rand)}"
                    emoji = getRandomEmoji(rand)
                    setPadding(20)
                    marginBetween = 30f
                    textSize = 40f
                    textColor = Color.WHITE
                    background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.emoji_view_background,
                        null
                    )
                }
            )
        }
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