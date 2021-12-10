package ru.s44khin.messenger.presentation.chat.adapter.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.s44khin.messenger.R
import ru.s44khin.messenger.presentation.chat.AdapterHandler
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.adapter.AdapterHelper
import ru.s44khin.messenger.views.FlexBoxLayout

class LeftAdapterDelegate(
    private val adapterHandler: AdapterHandler
) : AbsListItemAdapterDelegate<ChatItem, ChatItem, LeftAdapterDelegate.LeftViewHolder>() {

    private val adapterHelper = AdapterHelper(adapterHandler)

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.leftMessageAvatar)
        val cardView: CardView = itemView.findViewById(R.id.leftMessageCardView)
        val profile: TextView = itemView.findViewById(R.id.leftMessageProfile)
        val content: TextView = itemView.findViewById(R.id.leftMessageContent)
        val reactions: FlexBoxLayout = itemView.findViewById(R.id.leftMessageReactions)
    }

    override fun isForViewType(
        item: ChatItem,
        items: MutableList<ChatItem>,
        position: Int
    ) = !item.isMyMessage

    override fun onCreateViewHolder(parent: ViewGroup) = LeftViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_left_message, parent, false)
    )

    override fun onBindViewHolder(
        item: ChatItem,
        holder: LeftViewHolder,
        payloads: MutableList<Any>
    ) {
        Glide.with(holder.avatar)
            .load(item.avatar)
            .circleCrop()
            .into(holder.avatar)

        holder.content.text = item.content
        holder.profile.text = item.profile

        holder.avatar.setOnClickListener {
            adapterHandler.showProfile(item.avatar, item.profile, item.email)
        }

        holder.reactions.removeAllViews()

        if (item.reactions.isNotEmpty()) {
            holder.reactions.visibility = View.VISIBLE
            adapterHelper.addPlusButton(holder.reactions, item)
        } else {
            holder.reactions.visibility = View.GONE
        }

        for (reaction in item.reactions)
            adapterHelper.addReaction(
                messageId = item.id,
                reaction = reaction,
                flexBox = holder.reactions
            )

        holder.cardView.setOnLongClickListener {
            adapterHelper.showBottomSheet(it.context, item, holder.reactions)
        }

        holder.reactions.requestLayout()
    }
}