package ru.s44khin.coursework.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.s44khin.coursework.R

class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }

    private var text = ""
    private val textBounds = Rect()
    private val textCoordinate = PointF()
    private var emoji = ""
    private val emojiBounds = Rect()
    private val emojiCoordinate = PointF()
    private var marginBetween = 0f
    private val textPaint = Paint().apply {
        textAlign = Paint.Align.LEFT
        isAntiAlias = true
    }

    init {
        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiView,
            defStyleAttr,
            defStyleRes
        )

        text = attrsArray.getString(R.styleable.EmojiView_text) ?: ""
        emoji = attrsArray.getString(R.styleable.EmojiView_emoji) ?: ""
        marginBetween = attrsArray.getDimension(R.styleable.EmojiView_marginBetween, 0f)
        textPaint.color = attrsArray.getColor(R.styleable.EmojiView_textColor, Color.BLACK)
        textPaint.textSize = attrsArray.getDimension(R.styleable.EmojiView_textSize, 24f)

        attrsArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textPaint.getTextBounds(emoji, 0, emoji.length, emojiBounds)

        val totalWidth =
            textBounds.width() + emojiBounds.width() + paddingRight + paddingLeft + marginBetween
        val totalHeight =
            maxOf(textBounds.height(), emojiBounds.height()) + paddingTop + paddingBottom

        val resultWidth = resolveSize(totalWidth.toInt(), widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    /**
     * Текст крепится к левому краю, emoji к правому краю
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textCoordinate.x = paddingStart.toFloat()
        textCoordinate.y = h / 2f + textBounds.height() / 2f

        emojiCoordinate.x = (width - emojiBounds.width() - paddingEnd).toFloat()
        emojiCoordinate.y = h / 2f + textBounds.height() / 2f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, textPaint)
        canvas.drawText(emoji, emojiCoordinate.x, emojiCoordinate.y, textPaint)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState =
            super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)

        if (isSelected)
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)

        return drawableState
    }
}