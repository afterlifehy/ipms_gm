package com.rt.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class PagerStatesView : FrameLayout {
    constructor(conext: Context) : super(conext)
    constructor(conext: Context, attrs: AttributeSet) : super(conext, attrs)
    constructor(conext: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        conext,
        attrs,
        defStyleAttr
    )

    init {
        initView()
    }

    private fun initView() {

    }

    fun removeViewws() {
        if (getChildAt(0) != null) {
            removeAllViews()
        }
    }

    /**
     * 显示加载框效果
     */
    fun addLoadProgress(isShow: Boolean) {
        removeViewws()
        if (isShow) {
        } else {
            visibility = View.GONE
        }
    }

    interface OnPagerClickListener {
        fun onPagerClick()
    }
}