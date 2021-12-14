package ru.s44khin.messenger.presentation.chat.adapters

import android.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.MenuHandler
import ru.s44khin.messenger.presentation.chat.adapters.delegates.LeftAdapterDelegate
import ru.s44khin.messenger.presentation.chat.adapters.delegates.RightAdapterDelegate
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
    reactionSender: MenuHandler,
    color: String?
) : AsyncListDifferDelegationAdapter<ChatItem>(
    difItemCallback,
    LeftAdapterDelegate(reactionSender, if (color == null) null else Color.parseColor(color)),
    RightAdapterDelegate(reactionSender, if (color == null) null else Color.parseColor(color))
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
        super.onBindViewHolder(holder, position, payloads)
        paginationAdapterHelper.onBind(position, itemCount)
    }
}