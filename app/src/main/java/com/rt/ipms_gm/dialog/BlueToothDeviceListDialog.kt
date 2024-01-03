package com.rt.ipms_gm.dialog

import android.bluetooth.BluetoothDevice
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.rt.base.BaseApplication
import com.rt.base.bean.BlueToothDeviceBean
import com.rt.base.dialog.VBBaseLibDialog
import com.rt.base.help.ActivityCacheManager
import com.rt.ipms_gm.R
import com.rt.ipms_gm.adapter.BlueToothDeviceAdapter
import com.rt.ipms_gm.databinding.DialogStreetListBinding

class BlueToothDeviceListDialog(
    val deviceList: MutableList<BluetoothDevice>,
    var currentDevice: BlueToothDeviceBean?,
    val callback: BlueToothDeviceCallBack
) :
    VBBaseLibDialog<DialogStreetListBinding>(
        ActivityCacheManager.instance().getCurrentActivity()!!,
        com.rt.base.R.style.CommonBottomDialogStyle
    ), OnClickListener {

    var blueToothDeviceAdapter: BlueToothDeviceAdapter? = null

    init {
        initView()
    }

    private fun initView() {
        var height = (SizeUtils.dp2px(60f) * deviceList.size + SizeUtils.dp2px(217f)).toFloat()
        if (height > ScreenUtils.getAppScreenHeight() - BarUtils.getStatusBarHeight()) {
            height = (ScreenUtils.getAppScreenHeight() - BarUtils.getStatusBarHeight()).toFloat()
        }
        setLayout(getWidth(), height)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        binding.rvStreet.setHasFixedSize(true)
        binding.rvStreet.layoutManager = LinearLayoutManager(BaseApplication.instance())
        blueToothDeviceAdapter = BlueToothDeviceAdapter(deviceList, currentDevice)
        binding.rvStreet.adapter = blueToothDeviceAdapter

        binding.rtvOk.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rtv_ok -> {
                if (blueToothDeviceAdapter?.checkedDevice != null) {
                    callback.chooseDevice(blueToothDeviceAdapter?.checkedDevice)
                } else {
                    callback.chooseDevice(null)
                }
                dismiss()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return DialogStreetListBinding.inflate(layoutInflater)
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
        return true
    }

    override fun getGravity(): Int {
        return Gravity.BOTTOM
    }

    interface BlueToothDeviceCallBack {
        fun chooseDevice(device: BluetoothDevice?)
    }
}