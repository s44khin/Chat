package ru.s44khin.coursework.ui.chat

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Message
import ru.s44khin.coursework.ui.views.EmojiView
import ru.s44khin.coursework.ui.views.MessageView

class ChatAdapter(
    private val items: List<Message>
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageItem: MessageView = itemView.findViewById(R.id.messageItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val context = holder.messageItem.context

        holder.messageItem.profile.text = item.profile
        holder.messageItem.message.text = item.message
        holder.messageItem.alignment = item.alignment
        holder.messageItem.flexBoxLayout.removeAllViews()

        holder.messageItem.setOnLongClickListener {
            Toast.makeText(context, "long touch on $position", Toast.LENGTH_LONG).show()
            true
        }

        for (i in item.reactions.indices) {
            holder.messageItem.flexBoxLayout.addView(
                EmojiView(context).apply {
                    setPadding(20)
                    emoji = item.reactions[i].first
                    text = item.reactions[i].second.toString()

                    background = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.emoji_view_background,
                        null
                    )

                    setOnClickListener { emojiView ->
                        emojiView as EmojiView
                        val value = Integer.parseInt(emojiView.text)

                        if (emojiView.isSelected)
                            emojiView.text = (value - 1).toString()
                        else
                            emojiView.text = (value + 1).toString()

                        emojiView.isSelected = !emojiView.isSelected
                    }

                }
            )
        }
    }

    override fun getItemCount() = items.size
}