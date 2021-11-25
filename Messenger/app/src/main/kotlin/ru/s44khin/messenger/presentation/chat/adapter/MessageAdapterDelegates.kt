package ru.s44khin.messenger.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.s44khin.messenger.R
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.views.FlexBoxLayout
import ru.s44khin.messenger.views.MessageView

class MessageAdapterDelegate(
    private val layoutInflater: LayoutInflater
) : AbsListItemAdapterDelegate<
        ChatItem.Message,
        ChatItem,
        MessageAdapterDelegate.MessageViewHolder
        >() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageView: MessageView = itemView.findViewById(R.id.itemChatMessage)
        val avatar: ImageView = messageView.avatar
        val profile: TextView = messageView.profile
        val content: TextView = messageView.message
        val reactions: FlexBoxLayout = messageView.flexBoxLayout
    }

    override fun isForViewType(item: ChatItem, items: MutableList<ChatItem>, position: Int) =
        items[position] is ChatItem.Message

    override fun onCreateViewHolder(parent: ViewGroup) =
        MessageViewHolder(layoutInflater.inflate(R.layout.item_message, parent, false))

    override fun onBindViewHolder(
        item: ChatItem.Message,
        holder: MessageViewHolder,
        payloads: MutableList<Any>
    ) {

        holder.messageView.alignment =
            if (item.isMyMessage) MessageView.RIGHT else MessageView.LEFT

        holder.reactions.alignment =
            if (item.isMyMessage) FlexBoxLayout.RIGHT else FlexBoxLayout.LEFT

        Glide.with(holder.avatar)
            .load(item.avatar)
            .circleCrop()
            .into(holder.avatar)

        holder.profile.text = item.profile
        holder.content.text = item.content

        holder.reactions.removeAllViews()

        holder.messageView.requestLayout()
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