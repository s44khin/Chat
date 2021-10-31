package ru.s44khin.coursework.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Topic
import ru.s44khin.coursework.ui.chat.ChatActivity

class TopicAdapter(
    private val topics: List<Topic>
) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.topicName)
        val messages: TextView = itemView.findViewById(R.id.topicMessages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.topic_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        val context = holder.itemView.context
        holder.name.text = topic.name
        holder.messages.text = topic.messages.size.toString()

        holder.name.rootView.apply {
            setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow))
            setOnClickListener {
                context.startActivity(ChatActivity.createIntent(context))
            }
        }
    }

    override fun getItemCount() = topics.size
}