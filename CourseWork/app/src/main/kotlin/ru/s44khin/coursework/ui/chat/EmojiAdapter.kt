package ru.s44khin.coursework.ui.chat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.coursework.R

class EmojiAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<EmojiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emoji: TextView = itemView.findViewById(R.id.emoji_bottom_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.emoji_bottom_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.emoji.text = items[position]
        holder.emoji.setOnClickListener {
            val intent = Intent("EXTRA_EMOJI")
                .putExtra("EMOJI", items[position])

            LocalBroadcastManager.getInstance(holder.itemView.context).sendBroadcast(intent)
        }
    }

    override fun getItemCount() = items.size
}