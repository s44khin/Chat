package ru.s44khin.coursework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import ru.s44khin.coursework.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val LEFT = 0
        const val RIGHT = 1
    }

    var marginBetween = context.resources.getDimension(R.dimen.marginBetween).toInt()
    var alignment: Int

    init {
        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.FlexBoxLayout,
            defStyleAttr,
            defStyleRes
        )

        alignment = attrsArray.getInt(R.styleable.MessageView_alignment, 0)

        marginBetween = attrsArray.getDimension(
            R.styleable.FlexBoxLayout_marginBetween,
            context.resources.getDimension(R.dimen.marginBetween)
        ).toInt()

        attrsArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalWidth = paddingLeft + paddingRight
        var totalHeight = paddingTop + (getChildAt(0)?.measuredHeight ?: 0) + paddingBottom

        val width = MeasureSpec.getSize(widthMeasureSpec)

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)

            if (totalWidth + child.measuredWidth > width) {
                totalHeight += child.measuredHeight + marginBetween
                totalWidth = paddingLeft + paddingRight
            }

            totalWidth += child.measuredWidth + marginBetween
        }

        val resultWidth = resolveSize(width, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) = when(alignment) {
        LEFT -> onLayoutLeft()
        RIGHT -> onLayoutRight()
        else -> throw Exception("Expected \"left\" or \"right\", but received $alignment")
    }

    private fun onLayoutLeft() {
        var currentLeft = paddingLeft
        var currentTop = paddingTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (currentLeft + child.measuredWidth + paddingRight > width) {
                currentTop += child.measuredHeight + marginBetween
                currentLeft = paddingLeft
            }

            child.layout(
                currentLeft,
                currentTop,
                currentLeft + child.measuredWidth,
                currentTop + child.measuredHeight
            )

            currentLeft += child.measuredWidth + marginBetween
        }
    }

    private fun onLayoutRight() {
        var currentRight = width - paddingRight
        var currentTop = paddingTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (currentRight - child.measuredWidth - paddingLeft < 0) {
                currentTop += child.measuredHeight + marginBetween
                currentRight = width - paddingRight
            }

            child.layout(
                currentRight - child.measuredWidth,
                currentTop,
                currentRight,
                currentTop + child.measuredHeight
            )

            currentRight -= (child.measuredWidth + marginBetween)
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