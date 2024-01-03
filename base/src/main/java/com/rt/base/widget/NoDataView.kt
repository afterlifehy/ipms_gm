package com.rt.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingChild
import com.rt.base.R

class NoDataView : FrameLayout, NestedScrollingChild {
    constructor(
        conext: Context
    ) : super(conext)

    constructor(conext: Context, attrs: AttributeSet) : super(conext, attrs)
    constructor(conext: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        conext,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.no_data_layout, this)
        initView()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    private fun initView() {

    }

    override fun isNestedScrollingEnabled(): Boolean {
        return false
    }
}