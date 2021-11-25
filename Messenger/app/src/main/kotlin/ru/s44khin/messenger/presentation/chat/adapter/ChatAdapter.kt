package ru.s44khin.messenger.presentation.chat.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.ReactionSender
import ru.s44khin.messenger.presentation.chat.adapter.delegates.DateAdapterDelegate
import ru.s44khin.messenger.presentation.chat.adapter.delegates.LeftAdapterDelegate
import ru.s44khin.messenger.presentation.chat.adapter.delegates.RightAdapterDelegate
import ru.s44khin.messenger.presentation.chat.pagination.PaginationAdapterHelper

val difItemCallback = object : DiffUtil.ItemCallback<ChatItem>() {

    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        if (oldItem is ChatItem.Message && newItem is ChatItem.Date ||
            oldItem is ChatItem.Date && newItem is ChatItem.Message
        ) {
            return false
        }

        if (oldItem is ChatItem.Message && newItem is ChatItem.Message) {
            return oldItem.id == newItem.id
        }

        if (oldItem is ChatItem.Date && newItem is ChatItem.Date) {
            return oldItem.date == oldItem.date
        }

        return false
    }

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem) = true
}

class ChatAdapter(
    private val paginationAdapterHelper: PaginationAdapterHelper,
    reactionSender: ReactionSender,
    layoutInflater: LayoutInflater
) : AsyncListDifferDelegationAdapter<ChatItem>(
    difItemCallback,
    LeftAdapterDelegate(reactionSender),
    RightAdapterDelegate(reactionSender),
    DateAdapterDelegate(layoutInflater)
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        paginationAdapterHelper.onBind(position, itemCount)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        holder.itemView.requestLayout()
        super.onBindViewHolder(holder, position, payloads)
        paginationAdapterHelper.onBind(position, itemCount)
    }
}