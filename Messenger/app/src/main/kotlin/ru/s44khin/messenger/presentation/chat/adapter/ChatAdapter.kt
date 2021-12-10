package ru.s44khin.messenger.presentation.chat.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.AdapterHandler
import ru.s44khin.messenger.presentation.chat.adapter.delegates.LeftAdapterDelegate
import ru.s44khin.messenger.presentation.chat.adapter.delegates.RightAdapterDelegate
import ru.s44khin.messenger.presentation.chat.pagination.PaginationAdapterHelper

val difItemCallback = object : DiffUtil.ItemCallback<ChatItem>() {

    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ChatItem,
        newItem: ChatItem
    ): Boolean = true
}

class ChatAdapter(
    private val paginationAdapterHelper: PaginationAdapterHelper,
    reactionSender: AdapterHandler,
    layoutInflater: LayoutInflater
) : AsyncListDifferDelegationAdapter<ChatItem>(
    difItemCallback,
    LeftAdapterDelegate(reactionSender),
    RightAdapterDelegate(reactionSender)
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.requestLayout()
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