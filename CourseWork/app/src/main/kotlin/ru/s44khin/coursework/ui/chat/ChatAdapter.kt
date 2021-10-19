package ru.s44khin.coursework.ui.chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Message
import ru.s44khin.coursework.ui.views.EmojiView
import ru.s44khin.coursework.ui.views.FlexBoxLayout
import ru.s44khin.coursework.ui.views.MessageView
import ru.s44khin.coursework.utils.parse

class ChatAdapter(
    private val items: List<Any>
) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_MESSAGE_LEFT = 1
        const val TYPE_MESSAGE_RIGHT = 2
    }

    class DateViewHolder(itemView: View) : ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.chatDateItem)
    }

    class LeftViewHolder(itemView: View) : ViewHolder(itemView) {
        private val messageView: MessageView = itemView.findViewById(R.id.chatMessageLeft)
        val avatar: ImageView = messageView.avatar
        val profile: TextView = messageView.profile
        val message: TextView = messageView.message
        val flexBoxLayout: FlexBoxLayout = messageView.flexBoxLayout
    }

    class RightViewHolder(itemView: View) : ViewHolder(itemView) {
        private val messageView: MessageView = itemView.findViewById(R.id.chatMessageRight)
        val avatar: ImageView = messageView.avatar
        val profile: TextView = messageView.profile
        val message: TextView = messageView.message
        val flexBoxLayout: FlexBoxLayout = messageView.flexBoxLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_DATE -> {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_date_item, parent, false)
            DateViewHolder(itemView)
        }
        TYPE_MESSAGE_LEFT -> {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_message_left, parent, false)
            LeftViewHolder(itemView)
        }
        TYPE_MESSAGE_RIGHT -> {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_message_right, parent, false)
            RightViewHolder(itemView)
        }
        else -> throw Exception("Invalid view type")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        when (getItemViewType(position)) {
            TYPE_DATE -> {
                holder as DateViewHolder
                val item = items[position] as Int
                holder.date.text = parse(item)
            }
            TYPE_MESSAGE_LEFT -> {
                holder as LeftViewHolder
                val item = items[position] as Message
                val context = holder.message.context

                holder.profile.text = item.profile
                holder.message.text = item.message
                holder.flexBoxLayout.alignment = 0
                holder.flexBoxLayout.removeAllViews()

                if (item.reactions.size != 0)
                    LayoutInflater.from(context)
                        .inflate(R.layout.chat_add_button, holder.flexBoxLayout).apply {
                            setOnClickListener {
                                showEmojiBottomSheet(context, item, holder.flexBoxLayout)
                            }
                        }

                for ((emoji, text) in item.reactions)
                    holder.flexBoxLayout.addView(
                        addEmojiView(context, item, holder.flexBoxLayout, emoji, text)
                    )

                holder.message.setOnLongClickListener {
                    showEmojiBottomSheet(context, item, holder.flexBoxLayout)
                }
            }
            TYPE_MESSAGE_RIGHT -> {
                holder as RightViewHolder
                val item = items[position] as Message
                val context = holder.message.context

                holder.profile.text = item.profile
                holder.message.text = item.message
                holder.flexBoxLayout.alignment = 1
                holder.flexBoxLayout.removeAllViews()

                if (item.reactions.size != 0)
                    LayoutInflater.from(context)
                        .inflate(R.layout.chat_add_button, holder.flexBoxLayout).apply {
                            setOnClickListener {
                                showEmojiBottomSheet(context, item, holder.flexBoxLayout)
                            }
                        }

                for ((emoji, text) in item.reactions)
                    holder.flexBoxLayout.addView(
                        addEmojiView(context, item, holder.flexBoxLayout, emoji, text)
                    )

                holder.message.setOnLongClickListener {
                    showEmojiBottomSheet(context, item, holder.flexBoxLayout)
                }
            }
            else -> throw Exception("Invalid view type")
        }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = when (items[position]) {
        is Int -> TYPE_DATE
        is Message -> {
            val item = items[position] as Message
            if (item.alignment == 0) TYPE_MESSAGE_LEFT else TYPE_MESSAGE_RIGHT
        }
        else -> throw Exception("Invalid view type")
    }

    private fun addEmojiView(
        context: Context,
        item: Message,
        flexBoxLayout: FlexBoxLayout,
        emoji: String,
        text: Int,
        isSelected: Boolean = false
    ) = EmojiView(context).apply {
        setPadding(20)
        this.emoji = emoji
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
                    if (item.reactions[i].first == emojiView.emoji) {
                        item.reactions.removeAt(i)
                        flexBoxLayout.removeViewAt(i + 1)

                        if (flexBoxLayout.childCount == 1)
                            flexBoxLayout.removeAllViews()
                    }
        }
    }

    private fun showEmojiBottomSheet(
        context: Context,
        item: Message,
        flexBoxLayout: FlexBoxLayout
    ): Boolean {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        val emojiBottomSheet = EmojiBottomSheet()

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val emoji = p1?.getStringExtra("EMOJI") ?: ""
                fragmentManager.beginTransaction().remove(emojiBottomSheet).commit()

                var check = true

                for (i in item.reactions.indices)
                    if (item.reactions[i].first == emoji)
                        check = false

                if (check) {
                    if (flexBoxLayout.childCount == 0)
                        LayoutInflater.from(context)
                            .inflate(R.layout.chat_add_button, flexBoxLayout).apply {
                                setOnClickListener {
                                    showEmojiBottomSheet(context, item, flexBoxLayout)
                                }
                            }

                    flexBoxLayout.addView(
                        addEmojiView(context, item, flexBoxLayout, emoji, 1, true)
                    )
                }


                item.reactions.add(emoji to 1)
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
            }
        }

        val filter = IntentFilter()
        filter.addAction("EXTRA_EMOJI")
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter)

        emojiBottomSheet.show(fragmentManager, EmojiBottomSheet.TAG)
        return true
    }
}