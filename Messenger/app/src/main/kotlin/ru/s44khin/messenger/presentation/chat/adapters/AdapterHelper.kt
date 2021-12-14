package ru.s44khin.messenger.presentation.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.google.android.material.card.MaterialCardView
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.MenuHandler
import ru.s44khin.messenger.presentation.chat.bottomSheet.EmojiAdapter
import ru.s44khin.messenger.presentation.chat.bottomSheet.EmojiBottomSheet
import ru.s44khin.messenger.utils.hexToEmoji
import ru.s44khin.messenger.views.EmojiView
import ru.s44khin.messenger.views.FlexBoxLayout

class AdapterHelper(
    private val reactionSender: MenuHandler,
) {
    fun addPlusButton(flexBox: FlexBoxLayout, message: ChatItem) {
        LayoutInflater.from(flexBox.context).inflate(R.layout.item_add_button, flexBox).apply {
            setOnClickListener { showBottomSheet(flexBox.context, message, flexBox) }
        }
    }

    fun showBottomSheet(
        context: Context,
        message: ChatItem,
        flexBox: FlexBoxLayout,
    ): Boolean {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val emojiBottomSheet =
            EmojiBottomSheet.newInstance(message, reactionSender)

        fragmentManager.setFragmentResultListener(EmojiAdapter.REQUEST_KEY, context) { _, bundle ->
            val position = bundle.getInt(EmojiAdapter.RESULT_KEY)
            val emoji = MessengerApplication.instance.emojiList[position]
            fragmentManager.beginTransaction().remove(emojiBottomSheet).commit()

            var check = false

            for (elem in message.reactions)
                if (elem.emojiCode == emoji.first)
                    check = true

            if (check) {
                for (i in 1 until flexBox.childCount) {
                    val emojiView = flexBox.getChildAt(i) as EmojiView

                    if (emojiView.emoji == hexToEmoji(emoji.first)) {
                        emojiView.text = (Integer.parseInt(emojiView.text) + 1).toString()
                        emojiView.isSelected = true
                    }
                }
            } else {
                addReaction(
                    messageId = message.id,
                    reaction = AdapterReaction(
                        emojiCode = emoji.first,
                        emojiName = emoji.second,
                        count = 1,
                        iLiked = true
                    ),
                    flexBox = flexBox
                )
            }

            reactionSender.addReaction(message.id, emoji.second)
        }

        emojiBottomSheet.show(fragmentManager, EmojiBottomSheet.TAG)
        return true
    }

    fun addReaction(
        messageId: Int,
        reaction: AdapterReaction,
        flexBox: FlexBoxLayout
    ) {
        val reaction = LayoutInflater.from(flexBox.context)
            .inflate(R.layout.item_reaction_left, flexBox, false) as MaterialCardView

        flexBox.addView(reaction)
    }

    fun getLinkImage(content: String): String {
        val start = content.indexOf("/user_uploads/")
        var result = "https://tinkoff-android-fall21.zulipchat.com"

        for (i in start until content.length) {
            result += content[i]

            if (content[i + 1] == ')')
                break
        }

        return result
    }
}