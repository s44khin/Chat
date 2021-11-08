package ru.s44khin.messenger.ui.chat

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
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.AdapterMessage
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.ui.chat.bottomSheet.EmojiBottomSheet
import ru.s44khin.messenger.ui.views.EmojiView
import ru.s44khin.messenger.ui.views.FlexBoxLayout
import ru.s44khin.messenger.ui.views.MessageView

class ChatAdapter(
    private val messages: List<ChatItem>
) : RecyclerView.Adapter<ViewHolder>() {

    private enum class MessageType(val value: Int) {
        DATE(0),
        LEFT(1),
        RIGHT(2)
    }

    companion object {
        const val REQUEST_KEY = "RequestEmoji"
        const val RESULT_KEY = "ResultEmoji"
    }

    class DateViewHolder(itemView: View) : ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.chatDateItem)
    }

    abstract class MessageViewHolder(itemView: View) : ViewHolder(itemView) {
        abstract val avatar: ImageView
        abstract val profile: TextView
        abstract val message: TextView
        abstract val flexBoxLayout: FlexBoxLayout
    }

    class LeftViewHolder(itemView: View) : MessageViewHolder(itemView) {
        private val messageView: MessageView = itemView.findViewById(R.id.chatMessageLeft)
        override val avatar: ImageView = messageView.avatar
        override val profile: TextView = messageView.profile
        override val message: TextView = messageView.message
        override val flexBoxLayout: FlexBoxLayout = messageView.flexBoxLayout

        init {
            flexBoxLayout.alignment = FlexBoxLayout.LEFT
        }
    }

    class RightViewHolder(itemView: View) : MessageViewHolder(itemView) {
        private val messageView: MessageView = itemView.findViewById(R.id.chatMessageRight)
        override val avatar: ImageView = messageView.avatar
        override val profile: TextView = messageView.profile
        override val message: TextView = messageView.message
        override val flexBoxLayout: FlexBoxLayout = messageView.flexBoxLayout

        init {
            flexBoxLayout.alignment = FlexBoxLayout.RIGHT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        MessageType.DATE.value -> {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
            DateViewHolder(itemView)
        }
        MessageType.LEFT.value -> {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_left, parent, false)
            LeftViewHolder(itemView)
        }
        MessageType.RIGHT.value -> {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_right, parent, false)
            RightViewHolder(itemView)
        }
        else -> throw Exception("Invalid view type")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = when (messages[position]) {
        is ChatItem.DateItem -> {
            holder as DateViewHolder
            val item = messages[position] as ChatItem.DateItem
            holder.date.text = item.date
        }
        is ChatItem.MessageItem -> {
            holder as MessageViewHolder
            val message = (messages[position] as ChatItem.MessageItem).message
            val context = holder.message.context

            holder.profile.text = message.profile
            holder.message.text = message.content
            Glide.with(holder.avatar)
                .load(message.avatar)
                .circleCrop()
                .into(holder.avatar)
            holder.flexBoxLayout.removeAllViews()

            if (message.reactions.isNotEmpty())
                LayoutInflater.from(context)
                    .inflate(R.layout.item_add_button, holder.flexBoxLayout).apply {
                        setOnClickListener {
                            showBottomSheet(context, message, holder.flexBoxLayout)
                        }
                    }

            for (i in message.reactions.indices)
                holder.flexBoxLayout.addView(
                    addEmojiView(
                        message,
                        holder.flexBoxLayout,
                        message.reactions[i].emoji,
                        message.reactions[i].count,
                        message.reactions[i].iLiked
                    )
                )

            holder.message.setOnLongClickListener {
                showBottomSheet(context, message, holder.flexBoxLayout)
            }
        }
    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int) = when (messages[position]) {
        is ChatItem.DateItem -> MessageType.DATE.value
        is ChatItem.MessageItem -> {
            val item = messages[position] as ChatItem.MessageItem
            if (item.message.isMyMessage) MessageType.RIGHT.value else MessageType.LEFT.value
        }
    }

    private fun addEmojiView(
        item: AdapterMessage,
        flexBoxLayout: FlexBoxLayout,
        emoji: String,
        text: Int,
        isSelected: Boolean = false
    ) = EmojiView(flexBoxLayout.context).apply {
        setPadding((7 * context.resources.displayMetrics.density).toInt())
        this.emoji = String(Character.toChars(emoji.toInt(16)))
        this.text = text.toString()
        this.isSelected = isSelected
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
            } else {
                emojiView.text = (Integer.parseInt(emojiView.text) + 1).toString()
                emojiView.isSelected = true
            }

            if (emojiView.text == "0")
                for (i in item.reactions.lastIndex downTo 0)
                    if (item.reactions[i].emoji == emojiView.emoji) {
                        item.reactions.removeAt(i)
                        flexBoxLayout.removeViewAt(i + 1)

                        if (flexBoxLayout.childCount == 1)
                            flexBoxLayout.removeAllViews()
                    }
        }
    }

    private fun showBottomSheet(
        context: Context,
        item: AdapterMessage,
        flexBox: FlexBoxLayout
    ): Boolean {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val emojiBottomSheet = EmojiBottomSheet()

        fragmentManager.setFragmentResultListener(REQUEST_KEY, context) { _, bundle ->
            val emoji = bundle.getString(RESULT_KEY) ?: return@setFragmentResultListener
            fragmentManager.beginTransaction().remove(emojiBottomSheet).commit()

            var check = true

            for (i in item.reactions.indices)
                if (item.reactions[i].emoji == emoji)
                    check = false

            if (check) {
                if (flexBox.childCount == 0)
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_add_button, flexBox).apply {
                            setOnClickListener {
                                showBottomSheet(context, item, flexBox)
                            }
                        }

                flexBox.addView(
                    addEmojiView(item, flexBox, emoji, 1, true)
                )
            }

            item.reactions.add(AdapterReaction(emoji, 1, true))
        }

        emojiBottomSheet.show(fragmentManager, EmojiBottomSheet.TAG)
        return true
    }
}