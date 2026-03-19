package com.rt.common.view

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.rt.base.ext.hide
import com.rt.base.ext.show
import com.rt.common.databinding.ViewPlateNumBinding


class PlateNumView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    var binding: ViewPlateNumBinding? = null
    private var animatorSet: AnimatorSet? = null

    init {
        initView()
    }

    private fun initView() {
        binding = ViewPlateNumBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun getPlateView(): AppCompatTextView {
        return binding!!.tvPlate
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            performClick()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            performClick()
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun performSelectAnimation() {
//        val pivotX = (width / 2).toFloat()
//        val pivotY = (height / 2).toFloat()
//        setPivotX(pivotX)
//        setPivotY(pivotY)
//        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.5f)
//        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.5f)
//
////        scaleX.repeatCount = ValueAnimator.INFINITE
////        scaleX.repeatMode = ValueAnimator.REVERSE
////        scaleY.repeatCount = ValueAnimator.INFINITE
////        scaleY.repeatMode = ValueAnimator.REVERSE
//
//        animatorSet = AnimatorSet().apply {
//            playTogether(scaleX, scaleY)
//            duration = 600
//            interpolator = AccelerateDecelerateInterpolator()
//            start()
//        }
        binding!!.rflStroke.show()
    }

    fun stopSelectAnimation() {
        binding!!.rflStroke.hide()
//        animatorSet?.cancel()
//        this.scaleX = 1f
//        this.scaleY = 1f
    }
}