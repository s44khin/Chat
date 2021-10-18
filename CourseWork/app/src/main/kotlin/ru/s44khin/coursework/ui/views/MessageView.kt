package ru.s44khin.coursework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.s44khin.coursework.R

class MessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private var paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.message)
    }

    var avatar: ImageView
    var profile: TextView
    var message: TextView
    var flexBoxLayout: FlexBoxLayout
    var alignment: Int
    var messageCornerRadius: Int
    var messagePadding: Int
    var avatarMessageMargin: Int
    var messageFlexBoxMargin: Int

    init {
        inflate(context, R.layout.message_view, this)
        require(childCount == 4) { "Child count should be 4, but was $childCount" }

        avatar = getChildAt(0) as ImageView
        profile = getChildAt(1) as TextView
        message = getChildAt(2) as TextView
        flexBoxLayout = getChildAt(3) as FlexBoxLayout

        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MessageView,
            defStyleAttr,
            defStyleRes
        )

        alignment = attrsArray.getInt(R.styleable.MessageView_alignment, 0)

        messageCornerRadius = attrsArray.getDimension(
            R.styleable.MessageView_messageCornerRadius,
            context.resources.getDimension(R.dimen.cornerRadius)
        ).toInt()

        messagePadding = attrsArray.getDimension(
            R.styleable.MessageView_messagePadding,
            context.resources.getDimension(R.dimen.marginBetween)
        ).toInt()

        avatarMessageMargin = attrsArray.getDimension(
            R.styleable.MessageView_avatarMessageMargin,
            context.resources.getDimension(R.dimen.marginBetween)
        ).toInt()

        messageFlexBoxMargin = attrsArray.getDimension(
            R.styleable.MessageView_messageFlexBoxMargin,
            context.resources.getDimension(R.dimen.marginBetween)
        ).toInt()

        attrsArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalWidth = paddingLeft + paddingRight + 2 * messagePadding + avatarMessageMargin
        var totalHeight = paddingTop + paddingBottom + 2 * messagePadding + messageFlexBoxMargin

        measureChildWithMargins(avatar, widthMeasureSpec, 0, heightMeasureSpec, 0)
        totalWidth += avatar.measuredWidth
        measureChildWithMargins(profile, widthMeasureSpec, totalWidth, heightMeasureSpec, 0)
        totalHeight += profile.measuredHeight

        measureChildWithMargins(
            message,
            widthMeasureSpec,
            totalWidth,
            heightMeasureSpec,
            totalHeight
        )

        totalHeight += message.measuredHeight

        measureChildWithMargins(
            flexBoxLayout,
            widthMeasureSpec,
            totalWidth,
            heightMeasureSpec,
            totalHeight
        )

        totalHeight += flexBoxLayout.measuredHeight

        totalWidth += maxOf(
            profile.measuredWidth,
            message.measuredWidth,
            flexBoxLayout.measuredWidth
        )

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) = when (alignment) {
        0 -> onLayoutLeft()
        1 -> onLayoutRight()
        else -> throw Exception("Expected \"left\" or \"right\", but received $alignment")
    }

    private fun onLayoutLeft() {
        avatar.layout(
            paddingLeft,
            paddingTop,
            paddingLeft + avatar.measuredWidth,
            paddingTop + avatar.measuredHeight
        )

        profile.layout(
            paddingLeft + avatarMessageMargin + messagePadding + avatar.measuredWidth,
            paddingTop + messagePadding,
            paddingLeft + avatarMessageMargin + messagePadding +
                    avatar.measuredWidth + profile.measuredWidth,
            paddingTop + messagePadding + profile.measuredHeight
        )

        message.layout(
            paddingLeft + avatarMessageMargin + messagePadding + avatar.measuredWidth,
            paddingTop + messagePadding + profile.measuredHeight,
            paddingLeft + avatarMessageMargin + messagePadding +
                    avatar.measuredWidth + message.measuredWidth,
            paddingTop + messagePadding + profile.measuredHeight + message.measuredHeight
        )

        flexBoxLayout.layout(
            paddingLeft + avatarMessageMargin + avatar.measuredWidth,
            paddingTop + 2 * messagePadding + profile.measuredHeight +
                    message.measuredHeight + messageFlexBoxMargin,
            paddingLeft + avatarMessageMargin + avatar.measuredWidth + flexBoxLayout.measuredWidth,
            paddingTop + 2 * messagePadding + profile.measuredHeight +
                    messageFlexBoxMargin + message.measuredHeight + flexBoxLayout.measuredHeight
        )
    }

    private fun onLayoutRight() {
        avatar.layout(
            width - paddingRight - avatar.measuredWidth,
            paddingTop,
            width,
            paddingTop + avatar.measuredHeight
        )

        profile.layout(
            paddingLeft + messagePadding,
            paddingTop + messagePadding,
            width - paddingRight - avatar.measuredWidth - avatarMessageMargin - messagePadding,
            paddingTop + messagePadding + profile.measuredHeight
        )

        message.layout(
            paddingLeft + messagePadding,
            paddingTop + messagePadding + profile.measuredHeight,
            width - paddingRight - avatar.measuredWidth - avatarMessageMargin - messagePadding,
            paddingTop + messagePadding + profile.measuredHeight + message.measuredHeight
        )

        flexBoxLayout.layout(
            paddingLeft,
            paddingTop + 2 * messagePadding + profile.measuredHeight +
                    message.measuredHeight + messageFlexBoxMargin,
            width - avatar.measuredWidth - avatarMessageMargin,
            paddingTop + 2 * messagePadding + profile.measuredHeight +
                    message.measuredHeight + messageFlexBoxMargin + flexBoxLayout.measuredHeight
        )
    }

    override fun dispatchDraw(canvas: Canvas) = when (alignment) {
        0 -> {
            canvas.drawRoundRect(
                (paddingLeft + avatarMessageMargin + avatar.measuredWidth).toFloat(),
                paddingTop.toFloat(),
                (paddingLeft + avatarMessageMargin + messagePadding + avatar.measuredWidth + maxOf(
                    profile.measuredWidth,
                    message.measuredWidth
                )).toFloat(),
                (paddingTop + 2 * messagePadding +
                        profile.measuredHeight + message.measuredHeight).toFloat(),
                messageCornerRadius.toFloat(),
                messageCornerRadius.toFloat(),
                paint
            )
            super.dispatchDraw(canvas)
        }
        1 -> {
            canvas.drawRoundRect(
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                (width - avatar.measuredWidth - avatarMessageMargin).toFloat(),
                (paddingTop + 2 * messagePadding +
                        profile.measuredHeight + message.measuredHeight).toFloat(),
                messageCornerRadius.toFloat(),
                messageCornerRadius.toFloat(),
                paint
            )

            super.dispatchDraw(canvas)
        }
        else -> {
            super.dispatchDraw(canvas)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(layoutParams: LayoutParams): Boolean {
        return layoutParams is MarginLayoutParams
    }

    override fun generateLayoutParams(layoutParams: LayoutParams): LayoutParams {
        return MarginLayoutParams(layoutParams)
    }
}