package com.rt.ipms_gm.dialog

import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.rt.base.BaseApplication
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.help.ActivityCacheManager
import com.rt.ipms_gm.adapter.AbnormalClassificationAdapter
import com.rt.ipms_gm.databinding.DialogAbnormalClassificationBinding

class AbnormalClassificationDialog(
    val classificationList: MutableList<String>,
    var currentStreet: String,
    val callBack: AbnormalClassificationCallBack
) :
    VBBaseLibDialog<DialogAbnormalClassificationBinding>(ActivityCacheManager.instance().getCurrentActivity()!!) {
    var abnormalClassificationAdapter: AbnormalClassificationAdapter? = null

    init {
        initView()
    }

    private fun initView() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        binding.rvClassification.setHasFixedSize(true)
        binding.rvClassification.layoutManager = LinearLayoutManager(BaseApplication.instance())
        abnormalClassificationAdapter =
            AbnormalClassificationAdapter(
                classificationList,
                currentStreet,
                object : AbnormalClassificationAdapter.ChooseClassificationAdapterCallBack {
                    override fun chooseClassification(classification: String) {
                        callBack.chooseClassification(classification)
                        dismiss()
                    }
                })
        binding.rvClassification.adapter = abnormalClassificationAdapter
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogAbnormalClassificationBinding.inflate(layoutInflater)
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

    interface AbnormalClassificationCallBack {
        fun chooseClassification(classification: String)
    }
}