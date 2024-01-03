package com.rt.ipms_gm.ui.activity.parking

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSONObject
import com.tbruyelle.rxpermissions3.RxPermissions
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.PayResultBean
import com.rt.base.bean.PrintInfoBean
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.i18N
import com.rt.base.ext.i18n
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_gm.R
import com.rt.ipms_gm.databinding.ActivityPrepaidBinding
import com.rt.ipms_gm.dialog.PaymentQrDialog
import com.rt.ipms_gm.mvvm.viewmodel.PrepaidViewModel
import com.rt.common.event.RefreshParkingSpaceEvent
import com.rt.common.util.AppUtil
import com.rt.common.util.BluePrint
import com.rt.common.util.GlideUtils
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus

@Route(path = ARouterMap.PREPAID)
class PrepaidActivity : VbBaseActivity<PrepaidViewModel, ActivityPrepaidBinding>(), OnClickListener {
    var timeDuration = 1.0
    var paymentQrDialog: PaymentQrDialog? = null
    var qr = "www.baidu.com"

    var minAmount = 1.0
    var parkingNo = ""
    var carLicense = ""
    var orderNo = ""

    var simId = ""
    var loginName = ""

    var count = 0
    var handler = Handler(Looper.getMainLooper())
    var tradeNo = ""

    override fun initView() {
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.预支付)
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

        minAmount = intent.getDoubleExtra(ARouterMap.PREPAID_MIN_AMOUNT, 1.0)
        carLicense = intent.getStringExtra(ARouterMap.PREPAID_CARLICENSE).toString()
        parkingNo = intent.getStringExtra(ARouterMap.PREPAID_PARKING_NO).toString()
        orderNo = intent.getStringExtra(ARouterMap.PREPAID_ORDER_NO).toString()

        binding.tvPlate.text = carLicense
        binding.tvParkingNo.text = parkingNo?.replace("-", "")
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.rflAdd.setOnClickListener(this)
        binding.rflMinus.setOnClickListener(this)
        binding.rflScanPay.setOnClickListener(this)
    }

    override fun initData() {
        runBlocking {
            simId = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId)
            loginName = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.loginName)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.rfl_add -> {
                if (timeDuration == 99.0) {
                    return
                }
                timeDuration += 0.5
                binding.tvTimeDuration.text = timeDuration.toString()
            }

            R.id.rfl_minus -> {
                if (timeDuration == minAmount) {
                    return
                }
                timeDuration -= 0.5
                binding.tvTimeDuration.text = timeDuration.toString()
            }

            R.id.rfl_scanPay -> {
                val param = HashMap<String, Any>()
                val jsonobject = JSONObject()
                jsonobject["parkingNo"] = parkingNo
                jsonobject["orderNo"] = orderNo
                jsonobject["loginName"] = loginName
                jsonobject["simId"] = simId
                jsonobject["parkingHours"] = timeDuration.toString()
                jsonobject["orderType"] = "1"
                param["attr"] = jsonobject
                mViewModel.prePayFeeInquiry(param)
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            prePayFeeInquiryLiveData.observe(this@PrepaidActivity) {
                dismissProgressDialog()
                tradeNo = it.tradeNo
                paymentQrDialog = PaymentQrDialog(it.qrCode, AppUtil.keepNDecimals(it.totalAmount.toString(), 2))
                paymentQrDialog?.show()
                paymentQrDialog?.setOnDismissListener { handler.removeCallbacks(runnable) }
                count = 0
                handler.postDelayed(runnable, 2000)
            }
            payResultInquiryLiveData.observe(this@PrepaidActivity) {
                dismissProgressDialog()
                if (it != null) {
                    handler.removeCallbacks(runnable)
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.支付成功))
                    if (paymentQrDialog != null) {
                        paymentQrDialog?.dismiss()
                    }
                    val payResultBean = it
                    var rxPermissions = RxPermissions(this@PrepaidActivity)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        rxPermissions.request(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN).subscribe {
                            if (it) {
                                startPrint(payResultBean)
                            }
                        }
                    } else {
                        startPrint(payResultBean)
                    }
                    EventBus.getDefault().post(RefreshParkingSpaceEvent())
                    onBackPressedSupport()
                }
            }
            errMsg.observe(this@PrepaidActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
        }
    }

    val runnable = object : Runnable {
        override fun run() {
            if (count < 60) {
                checkPayResult()
                count++
                handler.postDelayed(this, 3000)
            }
        }
    }

    fun checkPayResult() {
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["simId"] = simId
        jsonobject["tradeNo"] = tradeNo
        param["attr"] = jsonobject
        mViewModel.payResultInquiry(param)
    }

    fun startPrint(it: PayResultBean) {
        val payMoney = it.payMoney
        val printInfo = PrintInfoBean(
            roadId = it.roadName,
            plateId = it.carLicense,
            payMoney = String.format("%.2f", payMoney.toFloat()),
            orderId = orderNo,
            phone = it.phone,
            startTime = it.startTime,
            leftTime = it.endTime,
            remark = it.remark,
            company = it.businessCname,
            oweCount = it.oweCount
        )
        ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.开始打印))
        Thread {
            BluePrint.instance?.zkblueprint(JSONObject.toJSONString(printInfo))
        }.start()
    }

    override fun providerVMClass(): Class<PrepaidViewModel> {
        return PrepaidViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityPrepaidBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override fun onStop() {
        super.onStop()
        if (handler != null) {
            handler.removeCallbacks(runnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler.removeCallbacks(runnable)
        }
    }
}