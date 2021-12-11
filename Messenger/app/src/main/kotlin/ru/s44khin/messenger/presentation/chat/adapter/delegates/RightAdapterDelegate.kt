package ru.s44khin.messenger.presentation.chat.adapter.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import okhttp3.Credentials
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.network.api.API_KEY
import ru.s44khin.messenger.data.network.api.EMAIL
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.MenuHandler
import ru.s44khin.messenger.presentation.chat.adapter.AdapterHelper
import ru.s44khin.messenger.views.FlexBoxLayout

class RightAdapterDelegate(
    reactionSender: MenuHandler
) : AbsListItemAdapterDelegate<ChatItem, ChatItem, RightAdapterDelegate.RightViewHolder>() {

    private val adapterHelper = AdapterHelper(reactionSender)

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.rightMessageContent)
        val image: ImageView = itemView.findViewById(R.id.rightMessageImage)
        val topicName: TextView = itemView.findViewById(R.id.rightMessageTopicName)
        val cardView: CardView = itemView.findViewById(R.id.rightMessageCardView)
        val reactions: FlexBoxLayout = itemView.findViewById(R.id.rightMessageReactions)
    }

    override fun isForViewType(
        item: ChatItem,
        items: MutableList<ChatItem>,
        position: Int
    ): Boolean {
        return item.isMyMessage
    }

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
            val credentials = Credentials.basic(EMAIL, API_KEY)

            val glideUrl = GlideUrl(
                linkImage,
                LazyHeaders.Builder()
                    .addHeader("Authorization", credentials)
                    .build()
            )

            Glide.with(holder.image)
                .load(glideUrl)
                .into(holder.image)

            holder.image.visibility = View.VISIBLE
        }

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