package ru.s44khin.messenger.presentation.main.members.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.s44khin.messenger.data.model.Profile

class MembersDiffUtilCallback(
    private val oldList: List<Profile>,
    private val newList: List<Profile>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return (oldItem.name == newItem.name &&
                oldItem.email == newItem.email &&
                oldItem.avatar == newItem.avatar)
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
}