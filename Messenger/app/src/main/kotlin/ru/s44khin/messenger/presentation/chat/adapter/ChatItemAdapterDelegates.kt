package ru.s44khin.messenger.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.s44khin.messenger.R
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.views.FlexBoxLayout
import ru.s44khin.messenger.views.MessageView


abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract val messageView: MessageView
    abstract val reactions: FlexBoxLayout
}

class LeftMessageAdapterDelegate(
    private val layoutInflater: LayoutInflater
) : AbsListItemAdapterDelegate<
        ChatItem.Message,
        ChatItem,
        LeftMessageAdapterDelegate.LeftMessageViewHolder
        >() {

    class LeftMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageView: MessageView = itemView.findViewById(R.id.itemChatMessageLeft)
        override val reactions: FlexBoxLayout = messageView.flexBoxLayout
    }

    override fun isForViewType(item: ChatItem, items: MutableList<ChatItem>, position: Int): Boolean {
        if (items[position] is ChatItem.Message)
            return if ((items[position] as ChatItem.Message).isMyMessage == false) true else false

        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup) =
        LeftMessageViewHolder(layoutInflater.inflate(R.layout.item_message_left, parent, false))

    override fun onBindViewHolder(
        item: ChatItem.Message,
        holder: LeftMessageViewHolder,
        payloads: MutableList<Any>
    ) {
        Glide.with(holder.messageView.avatar)
            .load(item.avatar)
            .circleCrop()
            .into(holder.messageView.avatar)

        holder.messageView.setMessage(item.content)
        holder.messageView.setProfile(item.profile)

        holder.reactions.removeAllViews()
    }
}

class RightMessageAdapterDelegate(
    private val layoutInflater: LayoutInflater
) : AbsListItemAdapterDelegate<
        ChatItem.Message,
        ChatItem,
        RightMessageAdapterDelegate.RightMessageViewHolder
        >() {

    class RightMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageView: MessageView = itemView.findViewById(R.id.itemChatMessageRight)
        override val reactions: FlexBoxLayout = messageView.flexBoxLayout
    }

    override fun isForViewType(item: ChatItem, items: MutableList<ChatItem>, position: Int): Boolean {
        if (items[position] is ChatItem.Message)
            return (items[position] as ChatItem.Message).isMyMessage

        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup) =
        RightMessageViewHolder(layoutInflater.inflate(R.layout.item_message_right, parent, false))

    override fun onBindViewHolder(
        item: ChatItem.Message,
        holder: RightMessageViewHolder,
        payloads: MutableList<Any>
    ) {
        Glide.with(holder.messageView.avatar)
            .load(item.avatar)
            .circleCrop()
            .into(holder.messageView.avatar)

        holder.messageView.setMessage(item.content)
        holder.messageView.setProfile(item.profile)

        holder.reactions.removeAllViews()
    }
}

class DateAdapterDelegate(
    private val layoutInflater: LayoutInflater
) : AbsListItemAdapterDelegate<ChatItem.Date, ChatItem, DateAdapterDelegate.DateViewHolder>() {

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.chatDateItem)
    }

    override fun isForViewType(
        item: ChatItem,
        items: MutableList<ChatItem>,
        position: Int
    ) = items[position] is ChatItem.Date

    override fun onCreateViewHolder(parent: ViewGroup) =
        DateViewHolder(layoutInflater.inflate(R.layout.item_date, parent, false))

    override fun onBindViewHolder(
        item: ChatItem.Date,
        holder: DateViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.date.text = item.date
    }
}