package ru.s44khin.messenger.presentation.streams.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.ResultStream

class StreamAdapter : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    var streams: List<ResultStream> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.streamName)
        val description: TextView = itemView.findViewById(R.id.streamDescription)
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
            description.text = stream.description
            if (description.text == "")
                description.visibility = View.GONE
            topics.adapter = TopicAdapter(stream.streamId, stream.name, stream.topics)

            itemView.setOnClickListener {
                topics.isVisible = !topics.isVisible
                notifyItemChanged(position, topics)
            }

            if (position == 0) {
                topics.isVisible = true
            }
        }
    }

    override fun getItemCount() = streams.size
}