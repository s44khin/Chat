package ru.s44khin.coursework.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = streams[position]

        holder.name.text = stream.name
        holder.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
            adapter = TopicAdapter(stream.topics)

            if (position == 0)
                visibility = View.VISIBLE
        }
        holder.name.rootView.setOnClickListener {
            holder.recyclerView.visibility = when (holder.recyclerView.visibility) {
                View.VISIBLE -> View.GONE
                View.GONE -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    override fun getItemCount() = streams.size
}