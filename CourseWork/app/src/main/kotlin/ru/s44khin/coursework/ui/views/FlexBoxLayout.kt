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

    private var marginBetween = 0f

    init {
        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.FlexBoxLayout,
            defStyleAttr,
            defStyleRes
        )

        marginBetween = attrsArray.getDimension(R.styleable.FlexBoxLayout_marginBetween, 0f)

        attrsArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalWidth = paddingLeft + paddingRight
        var totalHeight = paddingTop + paddingBottom

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            measureChildWithMargins(
                child,
                widthMeasureSpec,
                totalWidth,
                heightMeasureSpec,
                totalHeight
            )

            if (totalWidth + child.measuredWidth + marginBetween > MeasureSpec.getSize(widthMeasureSpec)) {
                totalWidth = 0
                totalHeight += child.measuredHeight
            }

            totalWidth += child.width + marginBetween.toInt()
        }

        val resultWidth = resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + getChildAt(0).height, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = paddingTop

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (currentLeft + child.measuredWidth + paddingRight > width) {
                currentTop += child.measuredHeight + marginBetween.toInt()
                currentLeft = paddingLeft
            }

            child.layout(
                currentLeft,
                currentTop,
                currentLeft + child.measuredWidth,
                currentTop + child.measuredHeight
            )

            currentLeft += child.measuredWidth + marginBetween.toInt()
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }
}