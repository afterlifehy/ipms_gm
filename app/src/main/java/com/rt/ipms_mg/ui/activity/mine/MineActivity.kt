package com.rt.ipms_mg.ui.activity.mine

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.AppUtils
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.BlueToothDeviceBean
import com.rt.base.bean.UpdateBean
import com.rt.base.dialog.DialogHelp
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.i18N
import com.rt.base.help.ActivityCacheManager
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.realm.RealmUtil
import com.rt.common.util.BluePrint
import com.rt.common.util.GlideUtils
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.ActivityMineBinding
import com.rt.ipms_mg.dialog.BlueToothDeviceListDialog
import com.rt.ipms_mg.mvvm.viewmodel.MineViewModel
import com.rt.ipms_mg.ui.activity.login.LoginActivity
import com.rt.ipms_mg.util.UpdateUtil
import com.tbruyelle.rxpermissions3.RxPermissions
import com.rt.base.ext.startArouter
import kotlinx.coroutines.runBlocking
import com.rt.ipms_mg.BuildConfig

@Route(path = ARouterMap.MINE)
class MineActivity : VbBaseActivity<MineViewModel, ActivityMineBinding>(), OnClickListener {
    var updateBean: UpdateBean? = null
    var blueToothDeviceListDialog: BlueToothDeviceListDialog? = null
    var currentDevice: BlueToothDeviceBean? = null
    var bluePrintStatus = 0
    var mineBluePrint = 0

    @SuppressLint("CheckResult", "MissingPermission")
    override fun initView() {
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.我的)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

        mineBluePrint = intent.getIntExtra(ARouterMap.MINE_BLUE_PRINT, 0)
        if (BuildConfig.is_dev) {
            binding.tvVersion.text = AppUtils.getAppVersionName() + " Dev"
        } else {
            binding.tvVersion.text = AppUtils.getAppVersionName()
        }
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.flBaseInfo.setOnClickListener(this)
        binding.flVersion.setOnClickListener(this)
        binding.flFeeRate.setOnClickListener(this)
        binding.flBlueToothPrint.setOnClickListener(this)
        binding.rtvLogout.setOnClickListener(this)
    }

    override fun initData() {
        if (RealmUtil.instance?.findCurrentDeviceList()!!.isNotEmpty()) {
            currentDevice = RealmUtil.instance?.findCurrentDeviceList()!![0]
            if (currentDevice != null) {
                binding.tvDeviceName.text = currentDevice?.name
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        Thread {
            if (BluePrint.instance != null && BluePrint.instance!!.zpSDK != null) {
                try {
                    BluePrint.instance!!.zpSDK?.printerStatus()
                    when (BluePrint.instance!!.zpSDK?.GetStatus()) {
                        -1 -> {
                            bluePrintStatus = -1
                            runOnUiThread {
                                BluePrint.instance!!.zpSDK?.disconnect()
                                binding.tvDeviceName.text = ""
                            }
                        }

                        0 -> {
                            bluePrintStatus = 0
                        }

                        1 -> {
                            bluePrintStatus = 1
                            runOnUiThread {
                                ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.打印机缺纸))
                            }
                        }

                        2 -> {
                            bluePrintStatus = 2
                            runOnUiThread {
                                ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.打印机开盖))
                            }
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
//                        BluePrint.instance!!.zpSDK?.disconnect()
                        binding.tvDeviceName.text = ""
                    }
                }
            }
        }.start()
        if (mineBluePrint == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                var rxPermissions = RxPermissions(this@MineActivity)
                rxPermissions.request(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN).subscribe {
                    if (it) {
                        showBlueToothDeviceListDialog()
                    }
                }
            } else {
                showBlueToothDeviceListDialog()
            }
            mineBluePrint = 0
        }
    }

    @SuppressLint("CheckResult", "MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.fl_baseInfo -> {
                startArouter(ARouterMap.BASE_INFO)
            }

            R.id.fl_version -> {
                val param = HashMap<String, Any>()
                val jsonobject = JSONObject()
                jsonobject["version"] = AppUtils.getAppVersionCode()
                jsonobject["softType"] = "15"
                param["attr"] = jsonobject
                mViewModel.checkUpdate(param)
            }

            R.id.fl_feeRate -> {
                startArouter(ARouterMap.FEE_RATE)
            }

            R.id.fl_blueToothPrint -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    var rxPermissions = RxPermissions(this@MineActivity)
                    rxPermissions.request(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN).subscribe {
                        if (it) {
                            showBlueToothDeviceListDialog()
                        }
                    }
                } else {
                    showBlueToothDeviceListDialog()
                }
            }

            R.id.rtv_logout -> {
                DialogHelp.Builder().setTitle(i18N(com.rt.base.R.string.是否确定进行不签退退出))
                    .setRightMsg(i18N(com.rt.base.R.string.确定)).setCancelable(true)
                    .setLeftMsg(i18N(com.rt.base.R.string.取消)).setCancelable(true)
                    .setOnButtonClickLinsener(object : DialogHelp.OnButtonClickLinsener {
                        override fun onLeftClickLinsener(msg: String) {
                        }

                        override fun onRightClickLinsener(msg: String) {
                            ARouter.getInstance().build(ARouterMap.LOGIN).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                            for (i in ActivityCacheManager.instance().getAllActivity()) {
                                if (i !is LoginActivity) {
                                    i.finish()
                                }
                            }
                            runBlocking {
                                PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.simId, "")
                                PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.phone, "")
                                PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.name, "")
                                PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.loginName, "")
                            }
                            RealmUtil.instance?.deleteAllStreet()
                        }

                    }).build(this@MineActivity).showDailog()
            }
        }
    }

    fun showBlueToothDeviceListDialog() {
        if (BluePrint.instance?.blueToothDevice!!.size > 0) {
            if (RealmUtil.instance?.findCurrentDeviceList()!!.isNotEmpty()) {
                currentDevice = RealmUtil.instance?.findCurrentDeviceList()!![0]
            }

            blueToothDeviceListDialog = BlueToothDeviceListDialog(
                BluePrint.instance?.blueToothDevice!!, if (bluePrintStatus == 0) currentDevice else null,
                object : BlueToothDeviceListDialog.BlueToothDeviceCallBack {
                    @SuppressLint("MissingPermission")
                    override fun chooseDevice(device: BluetoothDevice?) {
                        BluePrint.instance?.disConnect()
                        if (device != null) {
                            var connectResult = BluePrint.instance?.connet(device.address)
                            if (connectResult == 0) {
                                RealmUtil.instance?.deleteAllDevice()
                                RealmUtil.instance?.addRealm(BlueToothDeviceBean(device.address, device.name))
                                binding.tvDeviceName.text = device.name
                                bluePrintStatus = 0
                            } else {
                                bluePrintStatus = -1
                                return
                            }
                        } else {
                            bluePrintStatus = -1
                            binding.tvDeviceName.text = ""
                            ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.无打印机连接))
                        }
                    }
                })
            blueToothDeviceListDialog?.show()
        } else {
            DialogHelp.Builder().setTitle(i18N(com.rt.base.R.string.未检测到已配对的打印设备))
                .setLeftMsg(i18N(com.rt.base.R.string.取消))
                .setRightMsg(i18N(com.rt.base.R.string.去配对)).setCancelable(true)
                .setOnButtonClickLinsener(object : DialogHelp.OnButtonClickLinsener {
                    override fun onLeftClickLinsener(msg: String) {
                    }

                    override fun onRightClickLinsener(msg: String) {
                        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                        startActivity(intent)
                    }

                }).build(this@MineActivity).showDailog()
        }
    }

    @SuppressLint("CheckResult")
    fun requestPermissions() {
        var rxPermissions = RxPermissions(this@MineActivity)
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
            if (it) {
                UpdateUtil.instance?.downloadFileAndInstall(object : UpdateUtil.UpdateInterface {
                    override fun requestionPermission() {

                    }

                    override fun install(path: String) {
                        AppUtils.installApp(path)
                    }

                })
            } else {

            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            checkUpdateLiveDate.observe(this@MineActivity) {
                updateBean = it
                if (updateBean?.state == "0") {
                    UpdateUtil.instance?.checkNewVersion(updateBean!!, object : UpdateUtil.UpdateInterface {
                        override fun requestionPermission() {
                            requestPermissions()
                        }

                        override fun install(path: String) {
                        }
                    })
                } else {
                    ToastUtil.showMiddleToast("当前已是最新版本")
                }
            }
            errMsg.observe(this@MineActivity) {
                ToastUtil.showMiddleToast(it.msg)
            }
            mException.observe(this@MineActivity) {
                dismissProgressDialog()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityMineBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override fun providerVMClass(): Class<MineViewModel> {
        return MineViewModel::class.java
    }
}