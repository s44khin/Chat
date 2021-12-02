package ru.s44khin.messenger.presentation.main.streams.bottomMenu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import ru.s44khin.messenger.R
import ru.s44khin.messenger.presentation.main.streams.tabs.MenuHandler

class MenuAdapter(
    private val streamId: Int,
    private val streamName: String,
    private val description: String,
    private val forUnSubs: Boolean,
    private val color: String?,
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
            val context = holder.image.context

            holder.image.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    if (forUnSubs) R.drawable.ic_subscribe else R.drawable.ic_unsubscribe,
                    null
                )
            )
            holder.text.text = context.getString(
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
            val context = holder.image.context

            holder.image.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_color,
                    null
                )
            )
            holder.text.text = holder.image.context.getString(R.string.set_color)

            holder.text.rootView.setOnClickListener {
                ColorPickerDialogBuilder
                    .with(context)
                    .setTitle(context.getString(R.string.set_color))
                    .initialColor(Color.parseColor(color))
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .lightnessSliderOnly()
                    .setPositiveButton(context.getText(R.string.сonfirm)) { _, lastSelectedColor, _ ->
                        val strColor =
                            java.lang.String.format("#%06X", 0xFFFFFF.and(lastSelectedColor))
                        menuHandler.setStreamColor(streamId, strColor)
                        bottomMenuFragment.dismiss()
                    }
                    .build()
                    .show()
            }
        }
        else -> {}
    }

    override fun getItemCount() = if (forUnSubs) 1 else 2
}