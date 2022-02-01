package ru.s44khin.messenger.presentation.main.members.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.Profile
import ru.s44khin.messenger.presentation.main.members.OnClick

class MembersAdapter(
    private val onClick: OnClick,
    var members: List<Profile>
) : RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.peopleAvatar)
        val name: TextView = itemView.findViewById(R.id.peopleName)
        val email: TextView = itemView.findViewById(R.id.peopleEmail)
        val line: View = itemView.findViewById(R.id.peopleLine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = members[position]

        holder.apply {
            name.text = profile.name
            email.text = profile.email

            line.visibility = if (position == members.lastIndex) View.GONE else View.VISIBLE

            Glide.with(avatar)
                .load(profile.avatar)
                .circleCrop()
                .into(avatar)

            itemView.setOnClickListener {
                onClick.onClick(profile.avatar, profile.name, profile.email)
            }
        }
    }

    override fun getItemCount() = members.size
}