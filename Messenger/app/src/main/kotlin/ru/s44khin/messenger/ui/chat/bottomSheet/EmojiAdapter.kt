package ru.s44khin.messenger.ui.chat.bottomSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.messenger.R
import ru.s44khin.messenger.utils.hexToEmoji

class EmojiAdapter(
    private val emojis: List<Pair<String, String>>,
    private val fragment: BottomSheetDialogFragment
) : RecyclerView.Adapter<EmojiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emoji: TextView = itemView.findViewById(R.id.emoji_bottom_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.emoji_bottom_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emoji = emojis[position]

        holder.emoji.text = hexToEmoji(emoji.first)
        holder.emoji.setOnClickListener {
//            fragment.setFragmentResult(
//                ChatAdapter.REQUEST_KEY,
//                bundleOf(ChatAdapter.RESULT_KEY to position)
//            )
        }
    }

    override fun getItemCount() = emojis.size
}