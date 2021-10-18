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

    var marginBetween = context.resources.getDimension(R.dimen.marginBetween).toInt()

    init {
        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.FlexBoxLayout,
            defStyleAttr,
            defStyleRes
        )

        marginBetween = attrsArray.getDimension(
            R.styleable.FlexBoxLayout_marginBetween,
            context.resources.getDimension(R.dimen.marginBetween)
        ).toInt()

        attrsArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalWidth = paddingLeft + paddingRight
        var totalHeight = paddingTop + (getChildAt(0)?.measuredHeight ?: 0) + paddingBottom

        var maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

            if (totalWidth + child.measuredWidth > maxWidth) {
                totalHeight += child.measuredHeight + marginBetween
                totalWidth = paddingLeft + paddingRight
            }

            totalWidth += child.measuredWidth + marginBetween
        }

        val resultWidth = resolveSize(maxWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = paddingTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (currentLeft + child.measuredWidth + paddingLeft > width) {
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