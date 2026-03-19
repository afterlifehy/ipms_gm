package com.rt.ipms_mg.ui.activity.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.util.ArrayMap
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.hyperai.hyperlpr3.HyperLPR3
import com.hyperai.hyperlpr3.bean.Plate
import com.hyperai.hyperlpr3.settings.TypeDefine
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.ext.i18n
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.ActivityScanPlateBinding
import com.rt.ipms_mg.mvvm.viewmodel.ScanPlateViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = ARouterMap.SCAN_PLATE)
class ScanPlateActivity : VbBaseActivity<ScanPlateViewModel, ActivityScanPlateBinding>(), OnClickListener {
    var cameraPreview: CameraPreviews? = null
    var plateColorStr = ""
    var plateStr = ""
    var plateColor = com.rt.base.R.color.color_ff0046de
    var plateColorTxtMap: MutableMap<Int, String> = ArrayMap()
    var plateLogoColorMap: MutableMap<Int, Int> = ArrayMap()

    init {
        plateLogoColorMap[TypeDefine.PLATE_TYPE_UNKNOWN] = com.rt.base.R.color.white
        plateLogoColorMap[TypeDefine.PLATE_TYPE_BLUE] = com.rt.base.R.color.color_ff0046de
        plateLogoColorMap[TypeDefine.PLATE_TYPE_YELLOW_SINGLE] = com.rt.base.R.color.color_fffda027
        plateLogoColorMap[TypeDefine.PLATE_TYPE_WHILE_SINGLE] = com.rt.base.R.color.white
        plateLogoColorMap[TypeDefine.PLATE_TYPE_GREEN] = com.rt.base.R.color.color_ff09a95f
        plateLogoColorMap[TypeDefine.PLATE_TYPE_BLACK_HK_MACAO] = com.rt.base.R.color.black
        plateLogoColorMap[TypeDefine.PLATE_TYPE_HK_SINGLE] = com.rt.base.R.color.white
        plateLogoColorMap[TypeDefine.PLATE_TYPE_HK_DOUBLE] = com.rt.base.R.color.white
        plateLogoColorMap[TypeDefine.PLATE_TYPE_MACAO_SINGLE] = com.rt.base.R.color.white
        plateLogoColorMap[TypeDefine.PLATE_TYPE_MACAO_DOUBLE] = com.rt.base.R.color.white
        plateLogoColorMap[TypeDefine.PLATE_TYPE_YELLOW_DOUBLE] = com.rt.base.R.color.color_fffda027

        plateColorTxtMap[TypeDefine.PLATE_TYPE_UNKNOWN] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_BLUE] = i18n(com.rt.base.R.string.蓝牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_YELLOW_SINGLE] = i18n(com.rt.base.R.string.黄牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_WHILE_SINGLE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_GREEN] = i18n(com.rt.base.R.string.绿牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_BLACK_HK_MACAO] = i18n(com.rt.base.R.string.黑牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_HK_SINGLE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_HK_DOUBLE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_MACAO_SINGLE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_MACAO_DOUBLE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[TypeDefine.PLATE_TYPE_YELLOW_DOUBLE] = i18n(com.rt.base.R.string.黄牌)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(plates: Array<Plate>) {
        plateColorStr = ""
        plateStr = ""
        for (plate in plates) {
            plateColor = plate.type
            when (plateColor) {
                TypeDefine.PLATE_TYPE_UNKNOWN,
                TypeDefine.PLATE_TYPE_WHILE_SINGLE,
                TypeDefine.PLATE_TYPE_HK_SINGLE,
                TypeDefine.PLATE_TYPE_HK_DOUBLE,
                TypeDefine.PLATE_TYPE_MACAO_SINGLE,
                TypeDefine.PLATE_TYPE_MACAO_DOUBLE -> {
                    binding.rtvCarColor.delegate.setTextColor(ContextCompat.getColor(this@ScanPlateActivity, R.color.black))
                }

                else -> {
                    binding.rtvCarColor.delegate.setTextColor(ContextCompat.getColor(this@ScanPlateActivity, R.color.white))
                }
            }
            binding.rtvCarColor.delegate.setBackgroundColor(ContextCompat.getColor(BaseApplication.instance(), plateLogoColorMap[plateColor]!!))
            binding.rtvCarColor.delegate.init()
            plateColorStr = plateColorTxtMap[plate.type]!!
            plateStr = plate.code
            binding.rtvCarColor.text = plateColorStr
            binding.tvPlate.text = plateStr
        }
    }

    override fun initView() {
    }

    private fun initCamera() {
        cameraPreview = CameraPreviews(this)
        binding.flPreview.addView(cameraPreview)
    }

    override fun initListener() {
        binding.rflFlash.setOnClickListener(this)
        binding.rflOk.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rfl_flash -> {
                cameraPreview?.setFlashMode()
            }

            R.id.rfl_ok -> {
                val intent = Intent()
                intent.putExtra("plate", plateStr)
                intent.putExtra("plateColor", plateColor)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        var rxPermissions = RxPermissions(this@ScanPlateActivity)
        rxPermissions.request(Manifest.permission.CAMERA).subscribe {
            if (it) {
                if (cameraPreview == null) {
                    initCamera()
                }
            } else {
                onBackPressedSupport()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        cameraPreview == null
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityScanPlateBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun providerVMClass(): Class<ScanPlateViewModel>? {
        return ScanPlateViewModel::class.java
    }

    override fun isRegEventBus(): Boolean {
        return true
    }
}