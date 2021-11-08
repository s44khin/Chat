package ru.s44khin.coursework.ui.main.channels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Stream

class StreamAdapter(
    private val streams: List<Stream>
) : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.streamName)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.streamRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.stream_item, parent, false)

        val viewHolder = ViewHolder(itemView)
        viewHolder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(parent.context, LinearLayoutManager.VERTICAL, false)
            adapter = TopicAdapter("", listOf())
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = streams[position]

        holder.name.text = stream.name
        holder.recyclerView.apply {
            adapter = TopicAdapter(stream.name, stream.topics)

            if (position == 0)
                visibility = View.VISIBLE
        }
        holder.name.rootView.setOnClickListener {
            holder.recyclerView.isVisible = !holder.recyclerView.isVisible
        }
    }

    override fun getItemCount() = streams.size
}