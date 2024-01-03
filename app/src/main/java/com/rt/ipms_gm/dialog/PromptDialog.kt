package com.rt.ipms_gm.dialog

import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.ext.gone
import com.rt.base.help.ActivityCacheManager
import com.rt.ipms_gm.R
import com.rt.ipms_gm.databinding.DialogPromptBinding

class PromptDialog(
    val content: String,
    val leftString: String,
    val rightString: String,
    val callBack: PromptCallBack,
    val isSingleButton: Boolean = false,
) :
    VBBaseLibDialog<DialogPromptBinding>(ActivityCacheManager.instance().getCurrentActivity()!!), OnClickListener {

    init {
        initView()
    }

    private fun initView() {
        binding.tvContent.text = content
        binding.rtvLeft.text = leftString
        binding.rtvRight.text = rightString

        if (isSingleButton) {
            binding.rtvLeft.gone()
        }
        binding.rtvLeft.setOnClickListener(this)
        binding.rtvRight.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rtv_left -> {
                callBack.leftClick()
                dismiss()
            }

            R.id.rtv_right -> {
                callBack.rightClick()
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding? {
        return DialogPromptBinding.inflate(layoutInflater)
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

    interface PromptCallBack {
        fun leftClick()
        fun rightClick()
    }
}