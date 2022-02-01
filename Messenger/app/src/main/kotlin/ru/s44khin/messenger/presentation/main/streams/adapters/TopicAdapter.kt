package ru.s44khin.messenger.presentation.main.streams.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.presentation.chat.ChatActivity

class TopicAdapter(
    private val stream: ResultStream
) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.topicName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = stream.topics[position]

        holder.apply {
            name.text = topic.name

            itemView.setOnClickListener {
                itemView.context.startActivity(
                    ChatActivity.createIntent(
                        holder.itemView.context,
                        stream.streamId,
                        stream.name,
                        topic.name,
                        stream.color
                    )
                )
            }
        }
    }

    override fun getItemCount() = stream.topics.size
}