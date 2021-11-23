package ru.s44khin.messenger.presentation.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.bottomSheet.EmojiAdapter.Companion.REQUEST_KEY
import ru.s44khin.messenger.presentation.chat.bottomSheet.EmojiAdapter.Companion.RESULT_KEY
import ru.s44khin.messenger.presentation.chat.bottomSheet.EmojiBottomSheet
import ru.s44khin.messenger.presentation.chat.ReactionSender
import ru.s44khin.messenger.utils.hexToEmoji
import ru.s44khin.messenger.views.EmojiView
import ru.s44khin.messenger.views.FlexBoxLayout
import ru.s44khin.messenger.views.MessageView

class ChatAdapter(
    private val reactionSender: ReactionSender
) : RecyclerView.Adapter<ViewHolder>() {

    var messages: List<ChatItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private enum class TYPE(val value: Int) {
        DATE(0),
        LEFT(1),
        RIGHT(2)
    }

    class DateViewHolder(itemView: View) : ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.chatDateItem)
    }

    abstract class MessageViewHolder(itemView: View) : ViewHolder(itemView) {
        abstract val messageView: MessageView
        abstract val avatar: ImageView
        abstract val profile: TextView
        abstract val content: TextView
        abstract val reactions: FlexBoxLayout
    }

    class LeftViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageView: MessageView = itemView.findViewById(R.id.chatMessageLeft)
        override val avatar: ImageView = messageView.avatar
        override val profile: TextView = messageView.profile
        override val content: TextView = messageView.message
        override val reactions: FlexBoxLayout = messageView.flexBoxLayout

        init {
            reactions.alignment = FlexBoxLayout.LEFT
        }
    }

    class RightViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageView: MessageView = itemView.findViewById(R.id.chatMessageRight)
        override val avatar: ImageView = messageView.avatar
        override val profile: TextView = messageView.profile
        override val content: TextView = messageView.message
        override val reactions: FlexBoxLayout = messageView.flexBoxLayout

        init {
            reactions.alignment = FlexBoxLayout.RIGHT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE.DATE.value -> {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
            DateViewHolder(itemView)
        }
        TYPE.LEFT.value -> {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_left, parent, false)
            LeftViewHolder(itemView)
        }
        TYPE.RIGHT.value -> {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_right, parent, false)
            RightViewHolder(itemView)
        }
        else -> throw Exception("Invalid view type")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = when (messages[position]) {
        is ChatItem.Date -> {
            holder as DateViewHolder
            val item = messages[position] as ChatItem.Date
            holder.date.text = item.date
        }
        is ChatItem.Message -> {
            holder as MessageViewHolder
            val message = messages[position] as ChatItem.Message

            Glide.with(holder.avatar)
                .load(message.avatar)
                .circleCrop()
                .into(holder.avatar)

            holder.profile.text = message.profile
            holder.content.text = message.content

            holder.reactions.removeAllViews()

            if (message.reactions.isNotEmpty())
                holder.reactions.addPlusButton(message)

            for (reaction in message.reactions)
                holder.reactions.addReaction(
                    messageId = message.id,
                    reaction = reaction
                )

            holder.itemView.setOnLongClickListener {
                showBottomSheet(it.context, message, holder.reactions)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (messages[position]) {
        is ChatItem.Date -> TYPE.DATE.value
        is ChatItem.Message -> {
            val message = messages[position] as ChatItem.Message
            if (message.isMyMessage) TYPE.RIGHT.value else TYPE.LEFT.value
        }
    }

    override fun getItemCount() = messages.size

    private fun FlexBoxLayout.addPlusButton(message: ChatItem.Message) {
        LayoutInflater.from(context).inflate(R.layout.item_add_button, this).apply {
            setOnClickListener { showBottomSheet(context, message, this@addPlusButton) }
        }
    }

    private fun FlexBoxLayout.addReaction(messageId: Int, reaction: AdapterReaction) = addView(
        EmojiView(context).apply {
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
                    for (i in 1 until childCount)
                        if ((getChildAt(i) as EmojiView).emoji == emojiView.emoji) {
                            removeViewAt(i)

                            if (childCount == 1)
                                removeAllViews()
                        }
                }
            }
        }
    )

    private fun showBottomSheet(
        context: Context,
        message: ChatItem.Message,
        flexBox: FlexBoxLayout
    ): Boolean {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val emojiBottomSheet = EmojiBottomSheet()

        fragmentManager.setFragmentResultListener(REQUEST_KEY, context) { _, bundle ->
            val position = bundle.getInt(RESULT_KEY)
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
                flexBox.addReaction(
                    messageId = message.id,
                    reaction = AdapterReaction(
                        emojiCode = emoji.first,
                        emojiName = emoji.second,
                        count = 1,
                        iLiked = true
                    )
                )
            }

            reactionSender.addReaction(message.id, emoji.second)
        }

        emojiBottomSheet.show(fragmentManager, EmojiBottomSheet.TAG)
        return true
    }
}