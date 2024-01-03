package com.rt.common.util

import android.os.CountDownTimer

class CountDownUtil(
    millisInFuture: Long, countDownInterval: Long,
    private val timeCallBack: TimeCallBack?
) : CountDownTimer(millisInFuture, countDownInterval) {
    override fun onTick(millisUntilFinished: Long) {
        timeCallBack!!.onTimeTick(millisUntilFinished)
    }

    override fun onFinish() {
        cancel() //停止线程
        timeCallBack?.onTimeOut()
        //        view.get().setBackground(ContextCompat.getDrawable(BaseApplication.getInstance(), R.drawable.shape_getverify_code_green_bg));
    }

    interface TimeCallBack {
        fun onTimeOut()
        fun onTimeTick(millisUntilFinished: Long)
    }
}