package com.rt.ipms_gm.dialog

import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.rt.base.BaseApplication
import com.rt.base.bean.ExitMethodBean
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.help.ActivityCacheManager
import com.rt.ipms_gm.adapter.ExitMethodAdapter
import com.rt.ipms_gm.databinding.DialogExitMethodBinding

class ExitMethodDialog(
    val methodList: MutableList<ExitMethodBean>,
    var currentMethod: ExitMethodBean?,
    val callBack: ExitMethodCallBack
) :
    VBBaseLibDialog<DialogExitMethodBinding>(ActivityCacheManager.instance().getCurrentActivity()!!) {
    var exitMethodAdapter: ExitMethodAdapter? = null

    init {
        initView()
    }

    private fun initView() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        binding.rvExitMethod.setHasFixedSize(true)
        binding.rvExitMethod.layoutManager = LinearLayoutManager(BaseApplication.instance())
        exitMethodAdapter =
            ExitMethodAdapter(
                methodList,
                currentMethod,
                object : ExitMethodAdapter.ChooseExitMethodAdapterCallBack {
                    override fun chooseExitMethod(method: ExitMethodBean) {
                        callBack.chooseExitMethod(method)
                        dismiss()
                    }
                })
        binding.rvExitMethod.adapter = exitMethodAdapter
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogExitMethodBinding.inflate(layoutInflater)
    }

    override fun getHideInput(): Boolean {
        return true
    }

    override fun getWidth(): Float {
        return ScreenUtils.getScreenWidth() * 0.83f
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

    interface ExitMethodCallBack {
        fun chooseExitMethod(method: ExitMethodBean)
    }
}