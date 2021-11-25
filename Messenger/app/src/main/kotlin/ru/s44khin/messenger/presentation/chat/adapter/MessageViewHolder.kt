package ru.s44khin.messenger.presentation.chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.views.FlexBoxLayout
import ru.s44khin.messenger.views.MessageView

abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract val messageView: MessageView
    abstract val reactions: FlexBoxLayout
}