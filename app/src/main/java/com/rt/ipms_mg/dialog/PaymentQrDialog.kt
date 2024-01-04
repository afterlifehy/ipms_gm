package com.rt.ipms_mg.dialog

import android.view.Gravity
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.SizeUtils
import com.zrq.spanbuilder.TextStyle
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.ext.i18N
import com.rt.base.ext.i18n
import com.rt.base.help.ActivityCacheManager
import com.rt.common.util.CodeUtils
import com.rt.common.util.GlideUtils
import com.rt.ipms_mg.databinding.DialogPaymentQrBinding
import com.rt.common.util.AppUtil

class PaymentQrDialog(var qr: String, var amount: String) : VBBaseLibDialog<DialogPaymentQrBinding>(
    ActivityCacheManager.instance().getCurrentActivity()!!,
    com.rt.base.R.style.CommonBottomDialogStyle
) {
    val sizes = intArrayOf(19, 30, 19)
    val colors = intArrayOf(com.rt.ipms_mg.R.color.white, com.rt.ipms_mg.R.color.white, com.rt.ipms_mg.R.color.white)
    val styles = arrayOf(TextStyle.NORMAL, TextStyle.BOLD, TextStyle.NORMAL)

    init {
        initView()
    }

    private fun initView() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val qrBitmap = CodeUtils.createImage(qr, SizeUtils.dp2px(184f), SizeUtils.dp2px(184f), null)
        GlideUtils.instance?.loadImage(binding.rivQr, qrBitmap)
        val strings = arrayOf(i18N(com.rt.base.R.string.支付), amount, i18n(com.rt.base.R.string.元))
        binding.tvPayAmount.text = AppUtil.getSpan(strings, sizes, colors, styles)
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    override fun getVbBindingView(): ViewBinding? {
        return DialogPaymentQrBinding.inflate(layoutInflater)
    }

    override fun getHideInput(): Boolean {
        return true
    }

    override fun getWidth(): Float {
        return WindowManager.LayoutParams.MATCH_PARENT.toFloat()
    }

    override fun getHeight(): Float {
        return WindowManager.LayoutParams.WRAP_CONTENT.toFloat()
    }

    override fun getCanceledOnTouchOutside(): Boolean {
        return false
    }

    override fun getGravity(): Int {
        return Gravity.BOTTOM
    }
}