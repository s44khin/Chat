package ru.s44khin.messenger.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.ui.views.EmojiView
import ru.s44khin.messenger.ui.views.FlexBoxLayout
import ru.s44khin.messenger.ui.views.MessageView
import ru.s44khin.messenger.utils.emojiList

class ChatAdapter(
    private val messages: List<ChatItem>,
    private val viewModel: ChatViewModel
) : RecyclerView.Adapter<ViewHolder>() {

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
                holder.reactions.addPlusButton()

            for (reaction in message.reactions)
                holder.reactions.addReaction(
                    messageId = message.id,
                    reaction = reaction
                )
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

    private fun FlexBoxLayout.addPlusButton() {
        LayoutInflater.from(context).inflate(R.layout.item_add_button, this).apply {
            setOnClickListener {
                Toast.makeText(context, "Click)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun FlexBoxLayout.addReaction(messageId: Int, reaction: AdapterReaction) = addView(
        EmojiView(context).apply {
            this.emoji = String(Character.toChars(reaction.emojiCode.toInt(16)))
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
                val flexBoxLayout = parent as FlexBoxLayout

                if (emojiView.isSelected) {
                    emojiView.text = (Integer.parseInt(emojiView.text) - 1).toString()
                    emojiView.isSelected = false
                    viewModel.deleteReaction(messageId, reaction.emojiName)
                } else {
                    emojiView.text = (Integer.parseInt(emojiView.text) + 1).toString()
                    emojiView.isSelected = true
                    viewModel.addReaction(messageId, reaction.emojiName)
                }

                if (emojiView.text == "0") {
                    for (i in 1 until flexBoxLayout.childCount - 1)
                        if ((flexBoxLayout.getChildAt(i) as EmojiView).emoji == emojiView.emoji) {
                            flexBoxLayout.removeViewAt(i)

                            if (flexBoxLayout.childCount == 1)
                                flexBoxLayout.removeAllViews()
                        }
                }
            }
        }
    )
}