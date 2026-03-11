package com.rt.ipms_mg.dialog

import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.help.ActivityCacheManager
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.DialogPaymentMethodBinding

class PaymentMethodDialog(val callback: CashPayCallBack) :
    VBBaseLibDialog<DialogPaymentMethodBinding>(ActivityCacheManager.instance().getCurrentActivity()!!), OnClickListener {

    init {
        initView()
    }

    private fun initView() {
        binding.rtvQrcode.setOnClickListener(this)
        binding.rtvCash.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rtv_cash -> {
                callback.cash()
                dismiss()
            }

            R.id.rtv_qrcode -> {
                callback.qrCode()
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogPaymentMethodBinding.inflate(layoutInflater)
    }

    override fun getHideInput(): Boolean {
        return true
    }

    override fun getWidth(): Float {
        return ScreenUtils.getScreenWidth() * 0.9f
    }

    override fun getHeight(): Float {
        return WindowManager.LayoutParams.WRAP_CONTENT.toFloat()
    }

    override fun getCanceledOnTouchOutside(): Boolean {
        return true
    }

    override fun getGravity(): Int {
        return Gravity.CENTER
    }

    interface CashPayCallBack {
        fun cash()
        fun qrCode()
    }
}