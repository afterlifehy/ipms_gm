package com.rt.ipms_mg.ui.activity.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.hyperai.hyperlpr3.HyperLPR3
import com.hyperai.hyperlpr3.bean.Plate
import com.rt.base.arouter.ARouterMap
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
    var plateStr = ""

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(plates: Array<Plate>) {
        plateStr = ""
        for (plate in plates) {
            var type = "未知车牌"
            if (plate.type != HyperLPR3.PLATE_TYPE_UNKNOWN) {
                type = HyperLPR3.PLATE_TYPE_MAPS[plate.type]
            }
            val pStr = type + plate.code
            plateStr += pStr
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