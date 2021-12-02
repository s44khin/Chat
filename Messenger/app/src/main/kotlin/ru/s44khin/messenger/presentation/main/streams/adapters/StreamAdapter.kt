package ru.s44khin.messenger.presentation.main.streams.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.presentation.main.streams.tabs.PopupMenuHandler
import ru.s44khin.messenger.utils.parse2

class StreamAdapter(
    private val popupMenuHandler: PopupMenuHandler
) : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    var streams: List<ResultStream> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.streamDate)
        val name: TextView = itemView.findViewById(R.id.streamName)
        val tag: TextView = itemView.findViewById(R.id.streamTag)
        val more: ImageView = itemView.findViewById(R.id.streamMoreButton)
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
            if (stream.date == null)
                date.visibility = View.GONE
            else
                date.text = parse2(stream.date)

            name.text = stream.name
            description.text = stream.description

            if (stream.color != null) {
                name.setTextColor(Color.parseColor(stream.color))
                tag.setTextColor(Color.parseColor(stream.color))
                more.setOnClickListener { view ->
                    showMenu(
                        view = view,
                        menuRes = R.menu.unsubscribe_menu,
                        streamName = stream.name,
                        description = stream.description
                    )
                }
            } else {
                more.setOnClickListener { view ->
                    showMenu(
                        view = view,
                        menuRes = R.menu.subscribe_menu,
                        streamName = stream.name,
                        description = stream.description
                    )
                }
            }

            description.visibility = if (description.text == "")
                View.GONE
            else
                View.VISIBLE

            topics.adapter = TopicAdapter(stream.streamId, stream.name, stream.topics)
        }
    }

    private fun showMenu(
        view: View,
        @MenuRes menuRes: Int,
        streamName: String,
        description: String
    ) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.subscribe -> {
                    popupMenuHandler.subscribe(streamName, description)
                    true
                }
                R.id.unsubscribe -> {
                    popupMenuHandler.unsubscribe(streamName)
                    true
                }
                else -> {
                    false
                }
            }
        }

        popup.show()
    }

    override fun getItemCount() = streams.size
}