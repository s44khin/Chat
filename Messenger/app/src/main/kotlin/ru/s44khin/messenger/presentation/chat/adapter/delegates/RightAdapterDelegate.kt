package ru.s44khin.messenger.presentation.chat.adapter.delegates

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.network.GlideApp
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.MenuHandler
import ru.s44khin.messenger.presentation.chat.adapter.AdapterHelper
import ru.s44khin.messenger.views.FlexBoxLayout

class RightAdapterDelegate(
    reactionSender: MenuHandler,
    private val color: String?
) : AbsListItemAdapterDelegate<ChatItem, ChatItem, RightAdapterDelegate.RightViewHolder>() {

    private val adapterHelper = AdapterHelper(reactionSender)

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.rightMessageContent)
        val image: ImageView = itemView.findViewById(R.id.rightMessageImage)
        val topicName: TextView = itemView.findViewById(R.id.rightMessageTopicName)
        val cardView: MaterialCardView = itemView.findViewById(R.id.rightMessageCardView)
        val reactions: FlexBoxLayout = itemView.findViewById(R.id.rightMessageReactions)
    }

    override fun isForViewType(
        item: ChatItem,
        items: MutableList<ChatItem>,
        position: Int
    ) = item.isMyMessage

    override fun onCreateViewHolder(parent: ViewGroup) = RightViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_right_message, parent, false)
    )

    override fun onBindViewHolder(
        item: ChatItem,
        holder: RightViewHolder,
        payloads: MutableList<Any>
    ) {
        if (item.content.contains("](/user_uploads/")) {
            val linkImage = adapterHelper.getLinkImage(item.content)

            GlideApp.with(holder.image)
                .load(linkImage)
                .into(holder.image)

            holder.image.visibility = View.VISIBLE
        }

        holder.cardView.strokeColor = if (color == null)
            holder.itemView.context.getColor(R.color.tabBottom)
        else
            Color.parseColor(color)

        holder.content.text = item.content
        holder.topicName.text = item.topicName

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