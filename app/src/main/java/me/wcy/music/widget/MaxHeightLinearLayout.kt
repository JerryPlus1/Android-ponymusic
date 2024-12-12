package me.wcy.music.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import me.wcy.music.R


class MaxHeightLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var maxHeight: Int = 0

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightLinearLayout)
        maxHeight = ta.getDimensionPixelSize(R.styleable.MaxHeightLinearLayout_maxHeight, 0)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (maxHeight <= 0) {
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        val maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec)
    }

    
    fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
        requestLayout()
    }
}