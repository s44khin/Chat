package ru.s44khin.messenger.presentation.chat.adapters.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.network.GlideApp
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.MenuHandler
import ru.s44khin.messenger.presentation.chat.adapters.AdapterHelper
import ru.s44khin.messenger.presentation.chat.adapters.ReactionsAdapter

class RightAdapterDelegate(
    private val menuHandler: MenuHandler,
    @ColorInt private val color: Int?
) : AbsListItemAdapterDelegate<ChatItem, ChatItem, RightAdapterDelegate.RightViewHolder>() {

    private val adapterHelper = AdapterHelper(menuHandler)

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.rightMessageContent)
        val profile: TextView = itemView.findViewById(R.id.rightMessageProfile)
        val image: ImageView = itemView.findViewById(R.id.rightMessageImage)
        val topicName: TextView = itemView.findViewById(R.id.rightMessageTopicName)
        val cardView: MaterialCardView = itemView.findViewById(R.id.rightMessageCardView)
        val reactions: RecyclerView = itemView.findViewById(R.id.rightMessageReactions)

        init {
            reactions.layoutManager = FlexboxLayoutManager(itemView.context).apply {
                flexDirection = FlexDirection.ROW_REVERSE
                justifyContent = JustifyContent.FLEX_START
            }
        }
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
        val context = holder.itemView.context

        if (item.avatar == null) {
            holder.profile.visibility = View.GONE
        } else {
            holder.profile.visibility = View.VISIBLE
        }

        if (item.content.contains("](/user_uploads/")) {
            val linkImage = adapterHelper.getLinkImage(item.content)

            GlideApp.with(holder.image)
                .load(linkImage)
                .into(holder.image)

            holder.image.visibility = View.VISIBLE
        } else {
            holder.image.visibility = View.GONE
        }

        holder.profile.setTextColor(color ?: context.getColor(R.color.primary))

        holder.content.text = item.content
        holder.topicName.text = item.topicName

        if (item.reactions.isNotEmpty()) {
            holder.reactions.visibility = View.VISIBLE
            holder.reactions.adapter =
                ReactionsAdapter(
                    item,
                    ReactionsAdapter.Alignment.RIGHT,
                    menuHandler,
                    adapterHelper,
                    color
                )
        } else {
            holder.reactions.adapter = null
        }

        holder.cardView.setOnLongClickListener {
            adapterHelper.showBottomSheet(context, item)
        }
    }
}