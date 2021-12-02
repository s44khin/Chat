package ru.s44khin.messenger.presentation.main.streams.bottomMenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler

class MenuAdapter(
    private val streamName: String,
    private val description: String,
    private val forUnSubs: Boolean,
    private val menuHandler: MenuHandler,
    private val bottomMenuFragment: BottomMenuFragment
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.menuItemImage)
        val text: TextView = itemView.findViewById(R.id.menuItemText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = when (position) {
        0 -> {
            holder.image.setImageDrawable(
                ResourcesCompat.getDrawable(
                    holder.image.context.resources,
                    if (forUnSubs) R.drawable.ic_subscribe else R.drawable.ic_unsubscribe,
                    null
                )
            )
            holder.text.text = holder.image.context.getString(
                if (forUnSubs) R.string.subscribe else R.string.unsubscribe
            )

            holder.text.rootView.setOnClickListener {
                if (forUnSubs)
                    menuHandler.subscribe(streamName, description)
                else
                    menuHandler.unsubscribe(streamName)

                bottomMenuFragment.dismiss()
            }
        }
        1 -> {
            holder.image.setImageDrawable(
                ResourcesCompat.getDrawable(
                    holder.image.context.resources,
                    R.drawable.ic_color,
                    null
                )
            )
            holder.text.text = holder.image.context.getString(R.string.set_color)
        }
        else -> {}
    }

    override fun getItemCount() = if (forUnSubs) 1 else 2
}