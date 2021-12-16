package ru.s44khin.messenger.presentation.chat.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ru.s44khin.messenger.R
import ru.s44khin.messenger.data.model.AdapterReaction
import ru.s44khin.messenger.presentation.chat.ChatItem
import ru.s44khin.messenger.presentation.chat.MenuHandler
import ru.s44khin.messenger.utils.hexToEmoji

class ReactionsAdapter(
    private val message: ChatItem,
    private val alignment: Alignment,
    private val reactionSender: MenuHandler,
    private val adapterHelper: AdapterHelper,
    @ColorInt private val color: Int?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    sealed class Alignment {
        object LEFT : Alignment()
        object RIGHT : Alignment()
    }

    companion object {
        private const val FIRST_LEFT = 0
        private const val FIRST_RIGHT = 1
        private const val ITEM_LEFT = 2
        private const val ITEM_RIGHT = 3
    }

    abstract class ReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract val emoji: TextView
        abstract val count: TextView
        abstract val card: MaterialCardView
    }

    inner class ReactionLeftViewHolder(itemView: View) : ReactionViewHolder(itemView) {
        override val emoji: TextView = itemView.findViewById(R.id.itemReactionLeftEmoji)
        override val count: TextView = itemView.findViewById(R.id.itemReactionLeftText)
        override val card: MaterialCardView = itemView.findViewById(R.id.itemReactionLeftCardView)

        init {
            if (color != null) {
                card.rippleColor = ColorStateList.valueOf(color)
                card.setStrokeColor(ColorStateList.valueOf(color))
            }
        }
    }

    inner class ReactionRightViewHolder(itemView: View) : ReactionViewHolder(itemView) {
        override val emoji: TextView = itemView.findViewById(R.id.itemReactionRightEmoji)
        override val count: TextView = itemView.findViewById(R.id.itemReactionRightText)
        override val card: MaterialCardView = itemView.findViewById(R.id.itemReactionRightCardView)

        init {
            if (color != null) {
                card.rippleColor = ColorStateList.valueOf(color)
                card.setStrokeColor(ColorStateList.valueOf(color))
            }
        }
    }

    class FirstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        FIRST_LEFT -> {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reaction_left_plus, parent, false)
            FirstViewHolder(itemView)
        }
        FIRST_RIGHT -> {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reaction_right_plus, parent, false)
            FirstViewHolder(itemView)
        }
        ITEM_LEFT -> {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reaction_left, parent, false)
            ReactionLeftViewHolder(itemView)
        }
        else -> {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reaction_right, parent, false)
            ReactionRightViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (getItemViewType(position)) {
            FIRST_LEFT, FIRST_RIGHT -> {
                holder.itemView.setOnClickListener {
                    adapterHelper.showBottomSheet(holder.itemView.context, message)
                }
            }
            else -> {
                val reaction = message.reactions[position - 1]
                holder as ReactionViewHolder

                holder.apply {
                    emoji.text = try {
                        hexToEmoji(reaction.emojiCode)
                    } catch (error: Exception) {
                        reaction.emojiCode
                    }

                    card.isSelected = reaction.iLiked

                    count.text = reaction.count.toString()
                }

                holder.card.setOnClickListener {
                    if (reaction.iLiked)
                        removeReaction(holder, reaction)
                    else
                        addReaction(holder, reaction)

                    holder.card.isSelected = !holder.card.isSelected
                }
            }
        }

    override fun getItemCount() = message.reactions.size + 1

    override fun getItemViewType(position: Int): Int = when (alignment) {
        is Alignment.LEFT -> if (position == 0) FIRST_LEFT else ITEM_LEFT
        is Alignment.RIGHT -> if (position == 0) FIRST_RIGHT else ITEM_RIGHT
    }

    private fun removeReaction(holder: ReactionViewHolder, reaction: AdapterReaction) {
        reactionSender.removeReaction(message.id, reaction.emojiName)

        holder.count.text =
            (Integer.parseInt(holder.count.text.toString()) - 1).toString()

        holder.count.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.textColor
            )
        )

        holder.card.setCardBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.message
            )
        )
    }

    private fun addReaction(holder: ReactionViewHolder, reaction: AdapterReaction) {
        reactionSender.addReaction(message.id, reaction.emojiName)

        holder.count.text =
            (Integer.parseInt(holder.count.text.toString()) + 1).toString()
    }
}