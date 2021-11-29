package ru.s44khin.messenger.presentation.main.members.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.Profile

class MembersAdapter : RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    var members: List<Profile> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.peopleAvatar)
        val name: TextView = itemView.findViewById(R.id.peopleName)
        val email: TextView = itemView.findViewById(R.id.peopleEmail)
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

            Glide.with(avatar)
                .load(profile.avatar)
                .circleCrop()
                .into(avatar)

            itemView.setOnClickListener {
                Toast.makeText(it.context, "Click to ${profile.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = members.size
}