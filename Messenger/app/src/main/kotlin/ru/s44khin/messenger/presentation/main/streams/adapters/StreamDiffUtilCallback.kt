package ru.s44khin.messenger.presentation.main.streams.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.s44khin.messenger.data.model.ResultStream

class StreamDiffUtilCallback(
    private val oldList: List<ResultStream>,
    private val newList: List<ResultStream>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].streamId == newList[newItemPosition].streamId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return (oldItem.name == newItem.name &&
                oldItem.description == newItem.description &&
                oldItem.color == newItem.color &&
                oldItem.pinToTop == newItem.pinToTop &&
                oldItem.topics == newItem.topics)
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
}