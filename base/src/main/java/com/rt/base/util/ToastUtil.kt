package com.rt.base.util

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.view.Gravity
import com.aries.ui.view.radius.RadiusTextView
import com.rt.base.BaseApplication
import com.rt.base.R

/**
 * Created by hy on 2016/7/26.
 */
object ToastUtil {
    val instance: ToastUtil = ToastUtil
    var toast: Toast? = null
        private set

    fun showMiddleToast(msg: String?, icon: Int = -1) {
        val mView = ToastViewManager.get().getTostView(msg!!, icon)
        val mTost = ToastViewManager.get().getToast()
        mTost.view = mView
        mTost.show()
        Handler(Looper.getMainLooper()).postDelayed({ mTost.cancel() }, 1000)
    }

    /**
     * 成功消息提示
     */
    fun showSucessToast(msg: String?) {
        msg?.let {
            showMiddleToast(it, R.mipmap.ic_launcher)
        }
    }

    /**
     * 成功消息提示
     */
    fun showErrorToast(msg: String?) {
        msg?.let {
            showMiddleToast(it, R.mipmap.ic_launcher)

        }
    }

    fun showToast(text: String?) {
        if (!TextUtils.isEmpty(text)) {
            if (toast == null) {
                toast = Toast.makeText(BaseApplication.instance(), text, Toast.LENGTH_SHORT)
            } else {
//                View view = toast.getView();
//                TextView textView = view.findViewById(R.id.custom_toast_text);
//                textView.setText(msg);
                toast!!.setText(text)
            }
            toast!!.show()
        }
    }

    fun showTopToast(text: String?) {
        if (!TextUtils.isEmpty(text)) {
            if (toast == null) {
                toast = Toast(BaseApplication.instance())
            }
            val view = RadiusTextView(BaseApplication.instance())
            view.delegate.radius = 12f
            view.delegate.setBackgroundColor(ContextCompat.getColor(BaseApplication.instance(), R.color.black))
            view.delegate.setTextColor(ContextCompat.getColor(BaseApplication.instance(), R.color.white_90_color))
            view.text = text
            view.setPadding(18, 18, 18, 18)
            toast!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 40)
            toast!!.setView(view)
            toast!!.show()
        }
    }

    fun showMiddleToast(text: String?) {
        if (!TextUtils.isEmpty(text)) {
            toast = Toast(BaseApplication.instance())
            val view = RadiusTextView(BaseApplication.instance())
            view.delegate.radius = 12f
            view.delegate.setBackgroundColor(ContextCompat.getColor(BaseApplication.instance(), R.color.black))
            view.delegate.setTextColor(ContextCompat.getColor(BaseApplication.instance(), R.color.white_90_color))
            view.text = text
            view.setPadding(18, 18, 18, 18)
            toast!!.setGravity(Gravity.CENTER, 0, 0)
            toast!!.setView(view)
            toast!!.show()
        }
    }
}