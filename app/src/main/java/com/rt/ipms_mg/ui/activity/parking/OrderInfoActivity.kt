package com.rt.ipms_mg.ui.activity.parking

import android.Manifest
import android.annotation.SuppressLint
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
import com.zrq.spanbuilder.TextStyle
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.EndOrderInfoBean
import com.rt.base.bean.PayResultBean
import com.rt.base.bean.PrintInfoBean
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.i18N
import com.rt.base.ext.i18n
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.ActivityOrderInfoBinding
import com.rt.ipms_mg.dialog.PaymentQrDialog
import com.rt.ipms_mg.mvvm.viewmodel.OrderInfoViewModel
import com.rt.common.event.RefreshParkingSpaceEvent
import com.rt.common.util.AppUtil
import com.rt.common.util.BluePrint
import com.rt.common.util.GlideUtils
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus

@Route(path = ARouterMap.ORDER_INFO)
class OrderInfoActivity : VbBaseActivity<OrderInfoViewModel, ActivityOrderInfoBinding>(), OnClickListener {
    var paymentQrDialog: PaymentQrDialog? = null
    var qr = ""
    var endOrderBean: EndOrderInfoBean? = null
    var orderNo = ""

    val colors = intArrayOf(com.rt.base.R.color.white, com.rt.base.R.color.white)
    val sizes = intArrayOf(24, 19)
    val styles = arrayOf(TextStyle.BOLD, TextStyle.NORMAL)

    var simId = ""
    var loginName = ""
    var totalAmount = ""

    var count = 0
    var handler = Handler(Looper.getMainLooper())
    var tradeNo = ""
    var orderList: MutableList<String> = ArrayList()

    var isOrderCreate = false

    override fun initView() {
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.订单信息)
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

        orderNo = intent.getStringExtra(ARouterMap.ORDER_INFO_ORDER_NO).toString()
        orderList.add(orderNo)
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.rflAppPay.setOnClickListener(this)
        binding.rflRefusePay.setOnClickListener(this)
        binding.rflScanPay.setOnClickListener(this)
    }

    override fun initData() {
        runBlocking {
            simId = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId)
            loginName = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.loginName)
        }
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["orderNo"] = orderNo
        param["attr"] = jsonobject
        mViewModel.endOrderInfo(param)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.rfl_refusePay,
            R.id.rfl_appPay -> {
                if (!isOrderCreate) {
                } else {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.正在支付无法上传欠费))
                }
            }

            R.id.rfl_scanPay -> {
                if (!isOrderCreate) {
                    val param = HashMap<String, Any>()
                    val jsonobject = JSONObject()
                    jsonobject["orderNo"] = orderNo
                    jsonobject["totalAmount"] = totalAmount
                    jsonobject["loginName"] = loginName
                    jsonobject["simId"] = simId
                    jsonobject["orderType"] = "2"
                    param["attr"] = jsonobject
                    mViewModel.endOrderQR(param)
                } else {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.正在支付中))
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            endOrderInfoLiveData.observe(this@OrderInfoActivity) {
                dismissProgressDialog()
                endOrderBean = it
                totalAmount = endOrderBean?.realtimeMoney.toString()
                binding.tvCarLicense.text = endOrderBean?.carLicense
                val strings = arrayOf(endOrderBean?.orderMoney.toString(), "元")
                binding.tvOrderAmount.text = AppUtil.getSpan(strings, sizes, colors, styles)
                binding.tvPaidAmount.text = endOrderBean?.havePayMoney
                binding.rtvPayableAmount.text = endOrderBean?.realtimeMoney
            }
            endOrderQRLiveData.observe(this@OrderInfoActivity) {
                dismissProgressDialog()
                isOrderCreate = true
                tradeNo = it.tradeNo
                paymentQrDialog = PaymentQrDialog(it.qr_code, AppUtil.keepNDecimals(it.totalAmount.toString(), 2))
                paymentQrDialog?.show()
                paymentQrDialog?.setOnDismissListener { handler.removeCallbacks(runnable) }
                count = 0
                handler.postDelayed(runnable, 2000)
            }
            payResultInquiryLiveData.observe(this@OrderInfoActivity) {
                dismissProgressDialog()
                if (it != null) {
                    handler.removeCallbacks(runnable)
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.支付成功))
                    if (paymentQrDialog != null) {
                        paymentQrDialog?.dismiss()
                    }
                    val payResultBean = it
                    var rxPermissions = RxPermissions(this@OrderInfoActivity)
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
            errMsg.observe(this@OrderInfoActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
            mException.observe(this@OrderInfoActivity) {
                dismissProgressDialog()
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
            orderId = it.tradeNo,
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

    override fun providerVMClass(): Class<OrderInfoViewModel>? {
        return OrderInfoViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityOrderInfoBinding.inflate(layoutInflater)
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