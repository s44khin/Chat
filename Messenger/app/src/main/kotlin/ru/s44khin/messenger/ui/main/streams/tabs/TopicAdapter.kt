package ru.s44khin.messenger.ui.main.streams.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.Topic
import ru.s44khin.messenger.ui.chat.ChatActivity

class TopicAdapter(
    private val streamId: Int,
    private val streamName: String,
    private val topics: List<Topic>
) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.topicName)
        val messages: TextView = itemView.findViewById(R.id.topicMessages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]

        holder.apply {
            name.text = topic.name
//            messages.text = topic.messages.size.toString()
            itemView.setOnClickListener {
                itemView.context.startActivity(
                    ChatActivity.createIntent(itemView.context, streamId, streamName, topic.name)
                )
            }
        }
    }

    override fun getItemCount() = topics.size
}