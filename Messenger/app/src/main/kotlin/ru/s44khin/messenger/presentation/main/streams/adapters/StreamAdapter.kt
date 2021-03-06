package ru.s44khin.messenger.presentation.main.streams.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.ResultStream
import ru.s44khin.messenger.presentation.chat.ChatActivity
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler
import ru.s44khin.messenger.utils.parse2

class StreamAdapter(
    private val menuHandler: MenuHandler,
    var streams: List<ResultStream>
) : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.streamDate)
        val name: TextView = itemView.findViewById(R.id.streamName)
        val tag: TextView = itemView.findViewById(R.id.streamTag)
        val unpin: CardView = itemView.findViewById(R.id.streamUnpinButton)
        val more: CardView = itemView.findViewById(R.id.streamMoreButton)
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
            if (stream.pinToTop != null && stream.pinToTop == true) {
                unpin.visibility = View.VISIBLE
                unpin.setOnClickListener {
                    menuHandler.unpinFromTop(stream.streamId)
                }
            } else {
                unpin.visibility = View.GONE
            }

            date.text = parse2(stream.date)

            name.text = stream.name
            description.text = stream.description

            if (stream.color != null) {
                name.setTextColor(Color.parseColor(stream.color))
                tag.setTextColor(Color.parseColor(stream.color))
                more.setOnClickListener {
                    menuHandler.showMenu(stream)
                }
            } else {
                more.setOnClickListener {
                    menuHandler.showMenu(stream)
                }
            }

            itemView.setOnLongClickListener {
                menuHandler.showMenu(stream)
                true
            }

            itemView.setOnClickListener {
                itemView.context.startActivity(
                    ChatActivity.createIntent(
                        context = itemView.context,
                        streamId = stream.streamId,
                        streamName = stream.name,
                        topicName = null,
                        color = stream.color
                    )
                )
            }

            // ?????? description.text == "." ?????????? ???????????? ?????? ?????? ???????????? ??????????
            // ???????????????? ?????????? ???????????????? ".", ?? ???????? ???????????????????? ?????????????????????????? ??????????????
            description.visibility = if (description.text == "" || description.text == ".")
                View.GONE
            else
                View.VISIBLE

            topics.adapter = TopicAdapter(stream)
        }
    }

    override fun getItemCount() = streams.size
}