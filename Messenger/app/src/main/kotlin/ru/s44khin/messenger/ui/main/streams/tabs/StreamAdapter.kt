package ru.s44khin.messenger.ui.main.streams.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.data.model.Stream

class StreamAdapter(
    private val streams: List<ResultStream>
) : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.streamName)
        val topics: RecyclerView = itemView.findViewById(R.id.streamRecyclerView)

        init {
            topics.apply {
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_stream, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = streams[position]

        holder.apply {
            name.text = stream.name
            topics.adapter = TopicAdapter(stream.streamId, stream.name, stream.topics)

            itemView.setOnClickListener {
                topics.isVisible = !topics.isVisible
                notifyItemChanged(position, topics)
            }
        }
    }

    override fun getItemCount() = streams.size
}