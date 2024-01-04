package com.rt.ipms_mg.dialog

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.help.ActivityCacheManager
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.DialogSelectPicBinding

class SelectPicDialog(val callback: Callback) : VBBaseLibDialog<DialogSelectPicBinding>(
    ActivityCacheManager.instance().getCurrentActivity()!!,
    com.rt.base.R.style.CommonBottomDialogStyle
), View.OnClickListener {

    init {
        initView()
    }

    private fun initView() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        binding.rtvSelectFromAlbum.setOnClickListener(this)
        binding.rtvCancel.setOnClickListener(this)
        binding.tvTakePhoto.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rtv_selectFromAlbum -> callback.onPickPhoto()
            R.id.tv_takePhoto -> callback.onTakePhoto()
            R.id.rtv_cancel -> {
            }
        }
        dismiss()
    }

    override fun getHideInput(): Boolean {
        return false
    }

    override fun getWidth(): Float {
        return ScreenUtils.getScreenWidth() * 0.9f
    }

    override fun getHeight(): Float {
        return WindowManager.LayoutParams.WRAP_CONTENT.toFloat()
    }

    override fun getVbBindingView(): ViewBinding? {
        return DialogSelectPicBinding.inflate(layoutInflater)
    }

    override fun getCanceledOnTouchOutside(): Boolean {
        return false
    }

    override fun getGravity(): Int {
        return Gravity.BOTTOM
    }

    interface Callback {

        fun onTakePhoto()

        fun onPickPhoto()

    }
}