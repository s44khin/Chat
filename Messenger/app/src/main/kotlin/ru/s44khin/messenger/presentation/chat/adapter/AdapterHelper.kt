package ru.s44khin.messenger.presentation.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.ReactionSender
import ru.s44khin.messenger.presentation.chat.bottomSheet.EmojiAdapter
import ru.s44khin.messenger.presentation.chat.bottomSheet.EmojiBottomSheet
import ru.s44khin.messenger.utils.hexToEmoji
import ru.s44khin.messenger.views.EmojiView
import ru.s44khin.messenger.views.FlexBoxLayout

class AdapterHelper(
    private val reactionSender: ReactionSender,
) {
    fun addPlusButton(flexBox: FlexBoxLayout, message: ChatItem.Message) {
        LayoutInflater.from(flexBox.context).inflate(R.layout.item_add_button, flexBox).apply {
            setOnClickListener { showBottomSheet(flexBox.context, message, flexBox) }
        }
    }

    fun showBottomSheet(
        context: Context,
        message: ChatItem.Message,
        flexBox: FlexBoxLayout,
    ): Boolean {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val emojiBottomSheet = EmojiBottomSheet()

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
    ) = flexBox.addView(
        EmojiView(flexBox.context).apply {
            this.emoji = try {
                String(Character.toChars(reaction.emojiCode.toInt(16)))
            } catch (error: Exception) {
                reaction.emojiCode
            }

            this.text = reaction.count.toString()
            this.isSelected = reaction.iLiked
            setPadding((7 * context.resources.displayMetrics.density).toInt())
            background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.emoji_view_background,
                null
            )
            setOnClickListener { emojiView ->
                emojiView as EmojiView

                if (emojiView.isSelected) {
                    emojiView.text = (Integer.parseInt(emojiView.text) - 1).toString()
                    emojiView.isSelected = false
                    reactionSender.removeReaction(messageId, reaction.emojiName)
                } else {
                    emojiView.text = (Integer.parseInt(emojiView.text) + 1).toString()
                    emojiView.isSelected = true
                    reactionSender.addReaction(messageId, reaction.emojiName)
                }

                if (emojiView.text == "0") {
                    for (i in 1 until flexBox.childCount)
                        if ((flexBox.getChildAt(i) as EmojiView).emoji == emojiView.emoji) {
                            flexBox.removeViewAt(i)

                            if (flexBox.childCount == 1)
                                flexBox.removeAllViews()
                        }
                }
            }
        }
    )
}