package ru.s44khin.messenger.presentation.chat.adapter.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.s44khin.messenger.R
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.ReactionSender
import ru.s44khin.messenger.presentation.chat.adapter.AdapterHelper
import ru.s44khin.messenger.presentation.chat.adapter.MessageViewHolder
import ru.s44khin.messenger.views.FlexBoxLayout
import ru.s44khin.messenger.views.MessageView

class LeftAdapterDelegate(
    reactionSender: ReactionSender
) : AbsListItemAdapterDelegate<ChatItem.Message, ChatItem, LeftAdapterDelegate.LeftViewHolder>() {

    private val adapterHelper = AdapterHelper(reactionSender)

    class LeftViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageView: MessageView = itemView.findViewById(R.id.itemChatMessageLeft)
        override val reactions: FlexBoxLayout = messageView.flexBoxLayout

        init {
            messageView.alignment = MessageView.LEFT
            reactions.alignment = FlexBoxLayout.LEFT
        }
    }

    override fun isForViewType(
        item: ChatItem,
        items: MutableList<ChatItem>,
        position: Int
    ): Boolean {
        if (items[position] is ChatItem.Message)
            return !(items[position] as ChatItem.Message).isMyMessage

        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup) = LeftViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_message_left, parent, false)
    )

    override fun onBindViewHolder(
        item: ChatItem.Message,
        holder: LeftViewHolder,
        payloads: MutableList<Any>
    ) {
        Glide.with(holder.messageView.avatar)
            .load(item.avatar)
            .circleCrop()
            .into(holder.messageView.avatar)

        holder.messageView.message.text = item.content
        holder.messageView.profile.text = item.profile

        holder.reactions.removeAllViews()

        if (item.reactions.isNotEmpty())
            adapterHelper.addPlusButton(holder.reactions, item)

        for (reaction in item.reactions)
            adapterHelper.addReaction(
                messageId = item.id,
                reaction = reaction,
                flexBox = holder.reactions
            )

        holder.itemView.setOnLongClickListener {
            adapterHelper.showBottomSheet(it.context, item, holder.reactions)
        }

        holder.reactions.requestLayout()
    }
}