package com.rt.ipms_gm.dialog

import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.rt.base.bean.UpdateBean
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.ext.gone
import com.rt.base.ext.i18N
import com.rt.base.ext.show
import com.rt.base.help.ActivityCacheManager
import com.rt.ipms_gm.R
import com.rt.ipms_gm.databinding.DialogUpdateBinding

class UpdateDialog(var updateBean: UpdateBean, val callback: updateCallBack) :
    VBBaseLibDialog<DialogUpdateBinding>(ActivityCacheManager.instance().getCurrentActivity()!!), OnClickListener {

    init {
        initView()
    }

    private fun initView() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        binding.tvTitle.text = i18N(com.rt.base.R.string.发现新版本是否下载安装更新)
        if (updateBean.force == "1") {
            binding.rtvLeft.gone()
            setCancelable(false)
        } else if (updateBean.force == "0") {
            binding.rtvLeft.show()
            setCancelable(true)
        }
        binding.rtvRight.setOnClickListener(this)
        binding.rtvLeft.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rtv_right -> {
                callback.confirm()
            }

            R.id.rtv_left -> {
                dismiss()
            }
        }
    }

    fun downLoadUI() {
        binding.tvTitle.text = "正在下载 0%"
        binding.pbDownload.show()
        binding.llBtn.gone()
    }

    fun updateUI() {
        binding.tvTitle.text = i18N(com.rt.base.R.string.发现新版本是否下载安装更新)
        binding.pbDownload.gone()
        binding.llBtn.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateProgress(progress: Int) {
        binding.tvTitle.text = "正在下载 ${progress}%"
        binding.pbDownload.setProgress(progress, true)
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogUpdateBinding.inflate(layoutInflater)
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

    interface updateCallBack {
        fun confirm()
    }
}