package ru.s44khin.messenger.presentation.chat.adapters.delegates

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

class LeftAdapterDelegate(
    private val menuHandler: MenuHandler,
    @ColorInt private val color: Int?
) : AbsListItemAdapterDelegate<ChatItem, ChatItem, LeftAdapterDelegate.LeftViewHolder>() {

    private val adapterHelper = AdapterHelper(menuHandler)

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.leftMessageAvatar)
        val cardView: MaterialCardView = itemView.findViewById(R.id.leftMessageCardView)
        val image: ImageView = itemView.findViewById(R.id.leftMessageImage)
        val profile: TextView = itemView.findViewById(R.id.leftMessageProfile)
        val content: TextView = itemView.findViewById(R.id.leftMessageContent)
        val topicName: TextView = itemView.findViewById(R.id.leftMessageTopicName)
        val reactions: RecyclerView = itemView.findViewById(R.id.leftMessageReactions)

        init {
            reactions.layoutManager = FlexboxLayoutManager(itemView.context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
        }
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

        if (item.content.contains("](/user_uploads/")) {
            val linkImage = adapterHelper.getLinkImage(item.content)

            GlideApp.with(holder.image)
                .load(linkImage)
                .into(holder.image)

            holder.image.visibility = View.VISIBLE
        }

        holder.apply {
            cardView.strokeColor = if (color == null)
                itemView.context.getColor(R.color.tabBottom)
            else
                color

            content.text = item.content
            profile.text = item.profile
            topicName.text = item.topicName

            if (item.reactions.isNotEmpty()) {
                reactions.adapter =
                    ReactionsAdapter(
                        item,
                        ReactionsAdapter.Alignment.LEFT,
                        menuHandler,
                        adapterHelper,
                        color
                    )
            } else {
                reactions.adapter = null
            }

            avatar.setOnClickListener {
                menuHandler.showProfile(item.avatar, item.profile, item.email)
            }

            cardView.setOnLongClickListener {
                adapterHelper.showBottomSheet(itemView.context, item)
            }
        }
    }
}