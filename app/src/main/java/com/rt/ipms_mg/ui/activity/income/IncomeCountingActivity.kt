package com.rt.ipms_mg.ui.activity.income

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.IncomeCountingBean
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.i18N
import com.rt.base.util.ToastUtil
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.util.BluePrint
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.ActivityIncomeCountingBinding
import com.rt.ipms_mg.mvvm.viewmodel.IncomeCountingViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import com.rt.base.ext.show
import com.rt.ipms_mg.dialog.PromptDialog
import com.rt.common.util.GlideUtils
import com.rt.ipms_mg.pop.DatePop2
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat

@Route(path = ARouterMap.INCOME_COUNTING)
class IncomeCountingActivity : VbBaseActivity<IncomeCountingViewModel, ActivityIncomeCountingBinding>(), OnClickListener {
    var datePop: DatePop2? = null
    var startDate = ""
    var endDate = ""
    var incomeCountingBean: IncomeCountingBean? = null
    var loginName = ""
    var searchRange = "0"
    var promptDialog: PromptDialog? = null
    var sizes = intArrayOf(27, 19)

    override fun initView() {
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.营收盘点)
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivRight, com.rt.common.R.mipmap.ic_calendar)
        binding.layoutToolbar.ivRight.show()
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.layoutToolbar.ivRight.setOnClickListener(this)
        binding.tvTotalIncomeTitle.setOnClickListener(this)
        binding.rtvPrint.setOnClickListener(this)
    }

    override fun initData() {
        runBlocking {
            loginName = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.loginName)
        }
        endDate = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd")
        startDate = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd")
        binding.tvIncomeTime.text = startDate
        getIncomeCounting()
    }

    @SuppressLint("CheckResult")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.iv_right -> {
                if (datePop == null) {
                    datePop = DatePop2(BaseApplication.instance(), startDate, endDate, 1, object : DatePop2.DateCallBack {
                        override fun selectDate(startTime: String, endTime: String, type: Int) {
                            startDate = startTime
                            endDate = endTime
                            val difference = TimeUtils.getTimeSpan(endTime, startTime, SimpleDateFormat("yyyy-MM-dd"), TimeConstants.DAY)
                            if (difference > 90) {
                                ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.查询时间间隔不得超过90天))
                                return
                            }
                            if (type == 0 || type == 1) {
                                binding.tvIncomeTime.text = "${startDate}"
                                incomeCountingBean!!.range = startDate
                            } else {
                                binding.tvIncomeTime.text = "${startDate}~${endDate}"
                                incomeCountingBean!!.range = "${startDate}~${endDate}"
                            }
                            getIncomeCounting()
                        }

                    })
                }
                datePop?.showAsDropDown((v.parent) as Toolbar)
            }

            R.id.tv_totalIncomeTitle -> {
                promptDialog = PromptDialog(
                    i18N(com.rt.base.R.string.总收费和总收入均包含自主缴费金额和被追缴金额且不包含追缴他人金额),
                    "",
                    i18N(com.rt.base.R.string.确定),
                    object : PromptDialog.PromptCallBack {
                        override fun leftClick() {

                        }

                        override fun rightClick() {

                        }

                    }, true
                )
                promptDialog?.show()
            }

            R.id.rtv_print -> {
                incomeCountingBean?.loginName = loginName
                if (searchRange == "1") {
                    incomeCountingBean?.range = startDate + "~" + endDate
                }
                var str = "receipt,"
                var rxPermissions = RxPermissions(this@IncomeCountingActivity)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    rxPermissions.request(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN).subscribe {
                        if (it) {
                            val printList = BluePrint.instance?.blueToothDevice!!
                            if (printList.size == 1) {
                                Thread {
                                    val device = printList[0]
                                    var connectResult = BluePrint.instance?.connet(device.address)
                                    if (connectResult == 0) {
                                        runOnUiThread {
                                            ToastUtil.showBottomToast("开始打印")
                                        }
                                        BluePrint.instance?.zkblueprint(str + JSONObject.toJSONString(incomeCountingBean))
                                    }
                                }.start()
                            }
                        }
                    }
                } else {
                    val printList = BluePrint.instance?.blueToothDevice!!
                    if (printList.size == 1) {
                        Thread {
                            val device = printList[0]
                            var connectResult = BluePrint.instance?.connet(device.address)
                            if (connectResult == 0) {
                                runOnUiThread {
                                    ToastUtil.showBottomToast("开始打印")
                                }
                                BluePrint.instance?.zkblueprint(str + JSONObject.toJSONString(incomeCountingBean))
                            }
                        }.start()
                    }
                }
            }
        }
    }

    fun getIncomeCounting() {
        showProgressDialog(20000)
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["startDate"] = startDate
        jsonobject["endDate"] = endDate
        jsonobject["loginName"] = loginName
        jsonobject["searchRange"] = searchRange
        param["attr"] = jsonobject
        mViewModel.incomeCounting(param)
    }

    override fun startObserve() {
        mViewModel.apply {
            incomeCountingLiveData.observe(this@IncomeCountingActivity) {
                dismissProgressDialog()
                incomeCountingBean = it
                incomeCountingBean!!.payMoney = "200"
                incomeCountingBean!!.qrMoney = "100.00"
                incomeCountingBean!!.qrCount = "10"
                incomeCountingBean!!.cashMoney = "100.00"
                incomeCountingBean!!.cashCount = "99"
                incomeCountingBean!!.refundMoney = "10.00"
                incomeCountingBean!!.refundCount = "88"
                binding.tvTotalIncome.text = "${incomeCountingBean!!.payMoney}元"
                binding.tvQrPay.text = "${incomeCountingBean!!.qrMoney}元(${incomeCountingBean!!.qrCount}笔)"
                binding.tvCashPay.text = "${incomeCountingBean!!.cashMoney}(元${incomeCountingBean!!.cashCount}笔)"
                binding.tvRefund.text = "${incomeCountingBean!!.refundMoney}(元${incomeCountingBean!!.refundCount}笔)"
            }
            errMsg.observe(this@IncomeCountingActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
            mException.observe(this@IncomeCountingActivity) {
                dismissProgressDialog()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityIncomeCountingBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

    override fun providerVMClass(): Class<IncomeCountingViewModel> {
        return IncomeCountingViewModel::class.java
    }
}