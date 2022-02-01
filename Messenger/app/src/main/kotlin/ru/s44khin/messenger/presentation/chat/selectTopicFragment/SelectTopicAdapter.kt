package ru.s44khin.messenger.presentation.chat.selectTopicFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.Topic
import ru.s44khin.messenger.presentation.chat.MenuHandler

class SelectTopicAdapter(
    private val topics: List<Topic>,
    private val content: String,
    private val parentFragment: BottomSheetDialogFragment,
    private val menuHandler: MenuHandler
) : RecyclerView.Adapter<SelectTopicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.topicName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = topics[position].name

        holder.itemView.setOnClickListener {
            menuHandler.sendMessageToTopic(content, topics[position].name)
            parentFragment.dismiss()
        }
    }

    override fun getItemCount() = topics.size
}