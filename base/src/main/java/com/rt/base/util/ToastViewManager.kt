package com.rt.base.util

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.rt.base.BaseApplication
import com.rt.base.R

class ToastViewManager private constructor() {
    private var mToshView: View? = null
    private var mToast: Toast? = null
    private var ts_icon: ImageView? = null
    private var to_text: TextView? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ToastViewManager? = null
            get() {
                if (field == null) {
                    field = ToastViewManager()
                }
                return field
            }

        fun get(): ToastViewManager {
            return instance!!
        }
    }

    /**
     * 获取提示的view
     */
    fun getTostView(text: String = "", iconId: Int = -1): View {
        if (mToshView == null) {
            val mView = View.inflate(BaseApplication.instance(), R.layout.view_toast_layout, null)
            ts_icon = mView.findViewById(R.id.ts_icon)
            to_text = mView.findViewById(R.id.to_text)
            mToshView = mView
        }
        if (iconId == -1) {
            ts_icon?.visibility = View.GONE
        } else {
            ts_icon?.visibility = View.VISIBLE
            ts_icon?.setImageResource(iconId)
        }
        to_text?.text = text
        return mToshView!!
    }

    /**
     * 获取toast
     */
    fun getToast(): Toast {
        if (mToast == null) {
            val toat = Toast(BaseApplication.instance())
            toat.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 140)
            mToast = toat
        }
        return mToast!!
    }
}