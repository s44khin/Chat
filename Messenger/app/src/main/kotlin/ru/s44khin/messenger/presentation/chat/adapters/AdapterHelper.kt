package ru.s44khin.messenger.presentation.chat.adapters

import android.content.Context
import androidx.fragment.app.FragmentActivity
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.MenuHandler
import ru.s44khin.messenger.presentation.chat.bottomSheetEmoji.EmojiAdapter
import ru.s44khin.messenger.presentation.chat.bottomSheetEmoji.EmojiBottomSheet

class AdapterHelper(
    private val reactionSender: MenuHandler,
) {

    fun showBottomSheet(
        context: Context,
        message: ChatItem
    ): Boolean {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val emojiBottomSheet =
            EmojiBottomSheet.newInstance(message, reactionSender)

        fragmentManager.setFragmentResultListener(EmojiAdapter.REQUEST_KEY, context) { _, bundle ->
            val position = bundle.getInt(EmojiAdapter.RESULT_KEY)
            val emoji = MessengerApplication.instance.emojiList[position]
            fragmentManager.beginTransaction().remove(emojiBottomSheet).commit()
            reactionSender.addReaction(message.id, emoji.second)
        }

        emojiBottomSheet.show(fragmentManager, EmojiBottomSheet.TAG)
        return true
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