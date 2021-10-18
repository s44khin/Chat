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

    private var paint = Paint()
    private var textBounds = Rect()
    private var emojiBounds = Rect()
    private var textCoordinate = PointF()
    private var emojiCoordinate = PointF()

    var emoji = ""
    var text = ""
    var textColor = Color.BLACK
    var textSize = context.resources.getDimension(R.dimen.defaultTextSize)
    var marginBetween = context.resources.getDimension(R.dimen.marginBetween).toInt()

    init {
        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiView,
            defStyleAttr,
            defStyleRes
        )

        emoji = attrsArray.getString(R.styleable.EmojiView_emoji) ?: "❌"
        text = attrsArray.getString(R.styleable.EmojiView_text) ?: "0"
        textColor = attrsArray.getColor(R.styleable.EmojiView_textColor, Color.BLACK)

        textSize = attrsArray.getDimension(
            R.styleable.EmojiView_textSize,
            context.resources.getDimension(R.dimen.defaultTextSize)
        )

        marginBetween = attrsArray.getDimension(
            R.styleable.EmojiView_marginBetween,
            context.resources.getDimension(R.dimen.marginBetween)
        ).toInt()

        attrsArray.recycle()

        paint.apply {
            color = textColor
            textSize = this@EmojiView.textSize
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        paint.getTextBounds(emoji, 0, emoji.length, emojiBounds)
        paint.getTextBounds(text, 0, text.length, textBounds)

        val totalWidth =
            paddingLeft + emojiBounds.width() + marginBetween + textBounds.width() + paddingRight

        val totalHeight =
            paddingTop + maxOf(emojiBounds.height(), textBounds.height()) + paddingBottom

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        emojiCoordinate.x = paddingLeft.toFloat()
        emojiCoordinate.y = height / 2f + textBounds.height() / 2f

        textCoordinate.x = (paddingLeft + emojiBounds.width() + marginBetween).toFloat()
        textCoordinate.y = height / 2f + textBounds.height() / 2f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(emoji, emojiCoordinate.x, emojiCoordinate.y, paint)
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, paint)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState =
            super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)

        if (isSelected)
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)

        return drawableState
    }
}