package com.rt.ipms_mg.ui.activity.monthlypay

import android.annotation.SuppressLint
import android.util.ArrayMap
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSONObject
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.MonthlyOrderBean
import com.rt.base.ext.i18N
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.util.AppUtil
import com.rt.common.util.GlideUtils
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.ActivityMonthlyPrepaymentDetailBinding
import com.rt.ipms_mg.mvvm.viewmodel.MonthlyPrepayMentViewModel
import com.rt.base.util.ToastUtil
import com.rt.base.bean.PayResultBean
import com.rt.base.bean.PrintInfoBean
import kotlinx.coroutines.runBlocking
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.ext.hide
import com.rt.base.ext.i18n
import com.rt.base.ext.show
import com.rt.common.util.BluePrint
import com.rt.common.util.Constant
import com.rt.ipms_mg.dialog.CashPayDialog
import com.rt.ipms_mg.dialog.PaymentQrDialog

@Route(path = ARouterMap.MONTHLY_PREPAYMENT_DETAIL)
class MonthlyPrepaymentDetailActivity : VbBaseActivity<MonthlyPrepayMentViewModel, ActivityMonthlyPrepaymentDetailBinding>(),
    OnClickListener {
    val colors = intArrayOf(com.rt.base.R.color.color_ff666666, com.rt.base.R.color.color_ff1a1a1a)
    val sizes = intArrayOf(19, 19)
    var orderId = ""
    var simId = ""
    var loginName = ""
    var totalAmount = ""
    var isOrderCreate = false
    var cashPayDialog: CashPayDialog? = null
    var order: MonthlyOrderBean? = null
    var plateLogoColorMap: MutableMap<String, Int> = ArrayMap()
    var plateColorTxtMap: MutableMap<String, String> = ArrayMap()
    var paymentQrDialog: PaymentQrDialog? = null

    init {
        plateLogoColorMap[Constant.BLACK] = com.rt.base.R.color.black
        plateLogoColorMap[Constant.WHITE] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.GREY] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.RED] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.BLUE] = com.rt.base.R.color.color_ff0046de
        plateLogoColorMap[Constant.YELLOW] = com.rt.base.R.color.color_fffda027
        plateLogoColorMap[Constant.ORANGE] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.BROWN] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.GREEN] = com.rt.base.R.color.color_ff09a95f
        plateLogoColorMap[Constant.PURPLE] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.CYAN] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.PINK] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.TRANSPARENT] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.OTHERS] = com.rt.base.R.color.white
        plateLogoColorMap[Constant.OTHERS_OLD] = com.rt.base.R.color.white

        plateColorTxtMap[Constant.BLACK] = i18n(com.rt.base.R.string.黑牌)
        plateColorTxtMap[Constant.WHITE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.GREY] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.RED] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.BLUE] = i18n(com.rt.base.R.string.蓝牌)
        plateColorTxtMap[Constant.YELLOW] = i18n(com.rt.base.R.string.黄牌)
        plateColorTxtMap[Constant.ORANGE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.BROWN] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.GREEN] = i18n(com.rt.base.R.string.绿牌)
        plateColorTxtMap[Constant.PURPLE] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.CYAN] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.PINK] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.TRANSPARENT] = i18n(com.rt.base.R.string.白牌)
        plateColorTxtMap[Constant.OTHERS] = i18n(com.rt.base.R.string.临牌)
        plateColorTxtMap[Constant.OTHERS_OLD] = i18n(com.rt.base.R.string.白牌)
    }

    @SuppressLint("NewApi")
    override fun initView() {
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.text = "包月预付详情"
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

        orderId = intent.getStringExtra(ARouterMap.ORDER_ID).toString()

        // 显示车牌和金额
        binding.tvPlate.text = order?.carLicense

        order = generateFakeOrderData()

        if (order?.carColor == Constant.YELLOW_GREEN) {
            binding.llCarColor.show()
            binding.rtvCarColor.delegate.setStrokeWidth(0)
            binding.rtvCarColor.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    com.rt.base.R.color.transparent
                )
            )
            binding.rtvCarColor.text = "黄绿"
            binding.rtvCarColor.delegate.init()
        } else {
            binding.llCarColor.hide()
            binding.rtvCarColor.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(),
                    plateLogoColorMap[order?.carColor!!]!!
                )
            )
            binding.rtvCarColor.text = plateColorTxtMap[order?.carColor]
            if (plateLogoColorMap[order?.carColor]!! == com.rt.base.R.color.white) {
                binding.rtvCarColor.delegate.setStrokeWidth(1)
                binding.rtvCarColor.delegate.setTextColor(
                    ContextCompat.getColor(
                        BaseApplication.instance(),
                        com.rt.base.R.color.black
                    )
                )
            } else {
                binding.rtvCarColor.delegate.setStrokeWidth(0)
                binding.rtvCarColor.delegate.setTextColor(
                    ContextCompat.getColor(
                        BaseApplication.instance(),
                        com.rt.base.R.color.white
                    )
                )
            }
            binding.rtvCarColor.delegate.init()
        }

        binding.tvPlate.text = order?.carLicense
        // 显示详细信息
        val strings = arrayOf("订单号：", order?.orderNo.toString())
        binding.tvApplyId.text = AppUtil.getSpan(strings, sizes, colors)

        val strings1 = arrayOf("申请人：", order?.applyName.toString())
        binding.tvApplicant.text = AppUtil.getSpan(strings1, sizes, colors)

        val strings2 = arrayOf("街道：", order?.streetName.toString())
        binding.tvStreet.text = AppUtil.getSpan(strings2, sizes, colors)

        val strings3 = arrayOf("开始时间：", order?.startTime.toString())
        binding.tvStartTime.text = AppUtil.getSpan(strings3, sizes, colors)

        val strings4 = arrayOf("结束时间：", order?.endTime.toString())
        binding.tvEndTime.text = AppUtil.getSpan(strings4, sizes, colors)

        val strings5 = arrayOf("申请时间：", order?.startTime.toString())
        binding.tvApplyTime.text = AppUtil.getSpan(strings5, sizes, colors)

        val strings6 = arrayOf("金额：", order?.amount.toString() + "元")
        binding.tvAmount.text = AppUtil.getSpan(strings6, sizes, colors)

        // 根据支付状态显示不同颜色
        if (order?.status == "1") {
            binding.tvStatus.text = "状态：待审核"
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, com.rt.base.R.color.color_ff04a091))
        } else if (order?.status == "2") {
            binding.tvStatus.text = "状态：审核通过"
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, com.rt.base.R.color.color_fff38d00))
        } else if (order?.status == "3") {
            binding.tvStatus.text = "状态：审核拒绝"
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, com.rt.base.R.color.color_ffe92404))
        } else {
            binding.tvStatus.text = "状态：已完成"
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, com.rt.base.R.color.color_ff05d371))
        }
    }



    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.rtvCashPay.setOnClickListener(this)
        binding.rtvScanPay.setOnClickListener(this)
    }

    override fun initData() {
        runBlocking {
            simId = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId)
            loginName = PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.loginName)
            totalAmount = order?.amount.toString()
        }
    }

    private fun generateFakeOrderData(): MonthlyOrderBean {
        return MonthlyOrderBean(
            orderNo = "NO${System.currentTimeMillis()}",
            applyName = "张三",
            carLicense = "京 A88888",
            carColor = "5",
            startTime = "2026-03-19 08:00:00",
            endTime = "2027-03-18 23:59:59",
            applyTime = "2023-03-19 08:00:00",
            amount = "200.00",
            status = "1",
            streetName = "北京市朝阳区建国路 88 号",
            streetNo = "1",
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.rtv_cashPay -> {
                cashPayDialog =
                    CashPayDialog(totalAmount, object : CashPayDialog.CashPayCallBack {
                        override fun ok() {

                        }
                    })
                cashPayDialog?.show()
            }

            R.id.rtv_scanPay -> {
                if (!isOrderCreate) {
                    val param = HashMap<String, Any>()
                    val jsonobject = JSONObject()
                    jsonobject["orderNo"] = order?.orderNo
                    jsonobject["totalAmount"] = totalAmount
                    jsonobject["loginName"] = loginName
                    jsonobject["simId"] = simId
                    jsonobject["orderType"] = "2"
                    param["attr"] = jsonobject
                    // mViewModel.createOrderQR(param)
                    paymentQrDialog = PaymentQrDialog("", AppUtil.keepNDecimals("100", 2))
                    paymentQrDialog?.show()
                    paymentQrDialog?.setOnDismissListener { }
                } else {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.正在支付中))
                }
            }
        }
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
            oweCount = it.oweCount,
            qrcode = "12345"
        )
        ToastUtil.showMiddleToast(i18n(com.rt.base.R.string.开始打印))
        Thread {
            BluePrint.instance?.zkblueprint(JSONObject.toJSONString(printInfo))
        }.start()
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityMonthlyPrepaymentDetailBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }

    override fun providerVMClass(): Class<MonthlyPrepayMentViewModel> {
        return MonthlyPrepayMentViewModel::class.java
    }
}