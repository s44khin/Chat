package ru.s44khin.messenger.presentation.chat.adapter.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.s44khin.messenger.R
import ru.s44khin.messenger.presentation.chat.ChatItem

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