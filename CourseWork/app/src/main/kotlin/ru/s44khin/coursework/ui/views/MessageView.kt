package ru.s44khin.coursework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
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

    private val avatarBounds = Rect()
    private val profileBounds = Rect()
    private val messageBounds = Rect()
    private val flexBoxBounds = Rect()

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) = when (alignment) {
        0 -> onSizeChangedLeft()
        1 -> onSizeChangedRight()
        else -> throw Exception("Expected \"left\" or \"right\", but received $alignment")
    }

    private fun onSizeChangedLeft() {
        avatarBounds.apply {
            left = paddingLeft
            top = paddingTop
            right = avatar.measuredWidth
            bottom = avatar.measuredHeight
        }

        profileBounds.apply {
            left = paddingLeft + avatarMessageMargin + messagePadding + avatarBounds.width()
            top = paddingTop + messagePadding
            right = left + profile.measuredWidth
            bottom = top + profile.measuredHeight
        }

        messageBounds.apply {
            left = profileBounds.left
            top = profileBounds.bottom
            right = left + message.measuredWidth
            bottom = top + message.measuredHeight
        }

        flexBoxBounds.apply {
            left = profileBounds.left - messagePadding
            top = messageBounds.bottom + messageFlexBoxMargin + messagePadding
            right = left + flexBoxLayout.measuredWidth
            bottom = top + flexBoxLayout.measuredHeight
        }
    }

    private fun onSizeChangedRight() {
        avatarBounds.apply {
            right = width - paddingRight
            top = paddingTop
            left = right - avatar.measuredWidth
            bottom = top + avatar.measuredHeight
        }

        profileBounds.apply {
            right = avatarBounds.left - avatarMessageMargin - messagePadding
            top = paddingTop + messagePadding
            left = right - maxOf(profile.measuredWidth, message.measuredWidth)
            bottom = top + profile.measuredHeight
        }

        messageBounds.apply {
            right = profileBounds.right
            top = profileBounds.bottom
            left = right - maxOf(profile.measuredWidth, message.measuredWidth)
            bottom = top + message.measuredHeight
        }

        flexBoxBounds.apply {
            right = avatarBounds.left - avatarMessageMargin
            top = messageBounds.bottom + messageFlexBoxMargin + messagePadding
            left = right - maxOf(profile.measuredWidth, message.measuredWidth) - 2 * messagePadding
            bottom = top + flexBoxLayout.measuredHeight
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        avatar.layout(avatarBounds.left, avatarBounds.top, avatarBounds.right, avatarBounds.bottom)
        profile.layout(profileBounds.left, profileBounds.top, profileBounds.right, profileBounds.bottom)
        message.layout(messageBounds.left, messageBounds.top, messageBounds.right, messageBounds.bottom)
        flexBoxLayout.layout(flexBoxBounds.left, flexBoxBounds.top, flexBoxBounds.right, flexBoxBounds.bottom)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            (profileBounds.left - messagePadding).toFloat(),
            (profileBounds.top - messagePadding).toFloat(),
            (maxOf(profileBounds.right, messageBounds.right) + messagePadding).toFloat(),
            (messageBounds.bottom + messagePadding).toFloat(),
            messageCornerRadius.toFloat(),
            messageCornerRadius.toFloat(),
            paint
        )

        super.dispatchDraw(canvas)
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