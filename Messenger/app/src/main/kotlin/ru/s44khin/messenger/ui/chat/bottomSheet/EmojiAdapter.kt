package ru.s44khin.messenger.ui.chat.bottomSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.utils.hexToEmoji

class EmojiAdapter(
    private val fragment: BottomSheetDialogFragment
) : RecyclerView.Adapter<EmojiAdapter.ViewHolder>() {

    private val emojis = MessengerApplication.instance.emojiList

    companion object {
        const val REQUEST_KEY = "RequestEmoji"
        const val RESULT_KEY = "ResultEmoji"
    }

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
            fragment.setFragmentResult(
                REQUEST_KEY,
                bundleOf(RESULT_KEY to position)
            )
        }
    }

    override fun getItemCount() = emojis.size
}