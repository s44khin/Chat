package ru.s44khin.coursework.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.coursework.R

class EmojiAdapter(
    private val items: List<String>,
    private val fragment: BottomSheetDialogFragment
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
            fragment.setFragmentResult(
                ChatAdapter.REQUEST_KEY,
                bundleOf(ChatAdapter.RESULT_KEY to items[position])
            )
        }
    }

    override fun getItemCount() = items.size
}