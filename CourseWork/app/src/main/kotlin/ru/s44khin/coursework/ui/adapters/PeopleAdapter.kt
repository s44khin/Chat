package ru.s44khin.coursework.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.s44khin.coursework.R
import ru.s44khin.coursework.data.model.Profile


class PeopleAdapter(
    private val profiles: List<Profile>
) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.peopleAdapterAvatar)
        val name: TextView = itemView.findViewById(R.id.peopleAdapterProfile)
        val email: TextView = itemView.findViewById(R.id.peopleAdapterEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.people_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]

        holder.apply {
            holder.name.text = profile.name
            email.text = profile.email
            Glide.with(avatar)
                .load(profile.avatar)
                .centerCrop()
                .into(avatar)
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, "Click on ${profile.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = profiles.size
}