package com.rt.ipms_gm.ui.activity.order

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.OrderBean
import com.rt.base.ext.i18N
import com.rt.base.ext.i18n
import com.rt.base.ext.show
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.util.AppUtil
import com.rt.common.util.BigDecimalManager
import com.rt.common.util.GlideUtils
import com.rt.ipms_gm.R
import com.rt.ipms_gm.databinding.ActivityOrderDetailBinding
import com.rt.ipms_gm.mvvm.viewmodel.OrderDetailViewModel
import com.zrq.spanbuilder.TextStyle
import com.rt.base.dialog.DialogHelp
import com.rt.base.ext.gone
import com.rt.base.ext.startArouter
import com.rt.base.help.ActivityCacheManager
import com.rt.base.util.ToastUtil
import com.rt.common.event.RefreshOrderListEvent
import org.greenrobot.eventbus.EventBus

@Route(path = ARouterMap.ORDER_DETAIL)
class OrderDetailActivity : VbBaseActivity<OrderDetailViewModel, ActivityOrderDetailBinding>(), OnClickListener {
    val colors = intArrayOf(
        com.rt.base.R.color.color_ff04a091,
        com.rt.base.R.color.color_ff04a091,
        com.rt.base.R.color.color_ff04a091
    )
    val colors1 = intArrayOf(
        com.rt.base.R.color.color_ffe92404,
        com.rt.base.R.color.color_ffe92404,
        com.rt.base.R.color.color_ffe92404
    )
    val colors2 = intArrayOf(com.rt.base.R.color.color_ff666666, com.rt.base.R.color.color_ff1a1a1a)
    val sizes = intArrayOf(16, 24, 16)
    val sizes2 = intArrayOf(19, 19)
    var order: OrderBean? = null
    val styles = arrayOf(TextStyle.NORMAL, TextStyle.BOLD, TextStyle.NORMAL)
    val picList: MutableList<String> = ArrayList()

    @SuppressLint("NewApi")
    override fun initView() {
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.订单详细信息)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

        order = intent.getParcelableExtra(ARouterMap.ORDER) as? OrderBean
        binding.tvPlate.text = order?.carLicense
        if (order?.paidAmount?.toDouble()!! > 0.0) {
            val strings =
                arrayOf(i18n(com.rt.base.R.string.已付), order?.paidAmount.toString(), i18n(com.rt.base.R.string.元))
            binding.tvPayment.text = AppUtil.getSpan(strings, sizes, colors, styles)
            binding.rtvUpload.gone()
            binding.rtvTransactionRecord.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ff04a091
                )
            )
            binding.rtvTransactionRecord.setOnClickListener(this)
        } else if (order!!.paidAmount.toDouble() == 0.0 && order!!.amount.toDouble() == 0.0) {
            val strings = arrayOf(
                i18n(com.rt.base.R.string.已付),
                AppUtil.keepNDecimals(order?.paidAmount.toString(), 2),
                i18n(com.rt.base.R.string.元)
            )
            binding.tvPayment.text = AppUtil.getSpan(strings, sizes, colors, styles)
            binding.rtvUpload.gone()
            binding.rtvTransactionRecord.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffc5dddb
                )
            )
            binding.rtvTransactionRecord.setOnClickListener(null)
        } else {
            val strings = arrayOf(
                i18n(com.rt.base.R.string.欠),
                AppUtil.keepNDecimals(
                    BigDecimalManager.subtractionDoubleToString(
                        order?.amount!!.toDouble(),
                        order?.paidAmount!!.toDouble()
                    ), 2
                ),
                i18n(com.rt.base.R.string.元)
            )
            binding.tvPayment.text = AppUtil.getSpan(strings, sizes, colors1, styles)
            binding.rtvUpload.show()
            binding.rtvTransactionRecord.delegate.setBackgroundColor(
                ContextCompat.getColor(
                    BaseApplication.instance(), com.rt.base.R.color.color_ffc5dddb
                )
            )
            binding.rtvTransactionRecord.setOnClickListener(null)
            binding.rtvTransactionRecord.delegate.init()
            if (order?.isPrinted == "0") {
                binding.rtvUpload.delegate.setBackgroundColor(
                    ContextCompat.getColor(
                        BaseApplication.instance(), com.rt.base.R.color.color_ff04a091
                    )
                )
                binding.rtvUpload.setOnClickListener(this)
                binding.rtvUpload.delegate.init()
            } else {
                binding.rtvUpload.delegate.setBackgroundColor(
                    ContextCompat.getColor(
                        BaseApplication.instance(), com.rt.base.R.color.color_ffc5dddb
                    )
                )
                binding.rtvUpload.setOnClickListener(null)
                binding.rtvUpload.delegate.init()
            }
        }

        val strings1 = arrayOf(i18N(com.rt.base.R.string.订单) + "：", order?.orderNo.toString())
        binding.tvOrderNo.text = AppUtil.getSpan(strings1, sizes2, colors2)
        val strings2 = arrayOf(i18N(com.rt.base.R.string.泊位) + "：", order?.parkingNo.toString())
        binding.tvBerth.text = AppUtil.getSpan(strings2, sizes2, colors2)
        val strings3 = arrayOf(i18N(com.rt.base.R.string.路段) + "：", order?.streetName.toString())
        binding.tvStreet.text = AppUtil.getSpan(strings3, sizes2, colors2)
        val strings4 = arrayOf(i18N(com.rt.base.R.string.入场) + "：", order?.startTime.toString())
        binding.tvStartTime.text = AppUtil.getSpan(strings4, sizes2, colors2)
        val strings5 = arrayOf(i18N(com.rt.base.R.string.出场) + "：", order?.endTime.toString())
        binding.tvEndTime.text = AppUtil.getSpan(strings5, sizes2, colors2)
        val strings6 = arrayOf(i18N(com.rt.base.R.string.时长) + "：", AppUtil.dayHourMin(order?.duration!!.toInt()))
        binding.tvTotalTime.text = AppUtil.getSpan(strings6, sizes2, colors2)
        val strings7 = arrayOf(i18N(com.rt.base.R.string.总额) + "：", order?.amount.toString() + "元")
        binding.tvAmount.text = AppUtil.getSpan(strings7, sizes2, colors2)
    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
        binding.layoutToolbar.ivRight.setOnClickListener(this)
        binding.rtvDebtCollection.setOnClickListener(this)
        binding.rivPic1.setOnClickListener(this)
        binding.rivPic2.setOnClickListener(this)
        binding.rivPic3.setOnClickListener(this)
    }

    override fun initData() {
        showProgressDialog(20000)
        val param = HashMap<String, Any>()
        val jsonobject = JSONObject()
        jsonobject["orderNo"] = order?.orderNo
        param["attr"] = jsonobject
        mViewModel.picInquiry(param)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }

            R.id.rtv_debtCollection -> {
                ARouter.getInstance().build(ARouterMap.DEBT_COLLECTION).withString(ARouterMap.DEBT_CAR_LICENSE, order?.carLicense)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
            }

            R.id.rtv_transactionRecord -> {
                startArouter(ARouterMap.TRANSACTION_RECORD, data = Bundle().apply {
                    putString(ARouterMap.TRANSACTION_RECORD_ORDER_NO, order?.orderNo)
                })
            }

            R.id.rtv_upload -> {
                DialogHelp.Builder().setTitle(i18N(com.rt.base.R.string.是否立即上传))
                    .setLeftMsg(i18N(com.rt.base.R.string.取消))
                    .setRightMsg(i18N(com.rt.base.R.string.确定)).setCancelable(true)
                    .setOnButtonClickLinsener(object : DialogHelp.OnButtonClickLinsener {
                        override fun onLeftClickLinsener(msg: String) {
                        }

                        override fun onRightClickLinsener(msg: String) {
                            showProgressDialog(20000)
                            val param = HashMap<String, Any>()
                            val jsonobject = JSONObject()
                            jsonobject["orderNoList"] = order?.orderNo
                            param["attr"] = jsonobject
                            mViewModel.debtUpload(param)
                        }

                    }).build(ActivityCacheManager.instance().getCurrentActivity()).showDailog()
            }

            R.id.riv_pic1 -> {
                startArouter(ARouterMap.PREVIEW_IMAGE, data = Bundle().apply {
                    putInt(ARouterMap.IMG_INDEX, 0)
                    putStringArrayList(ARouterMap.IMG_LIST, picList as ArrayList<String>)
                })
            }

            R.id.riv_pic2 -> {
                startArouter(ARouterMap.PREVIEW_IMAGE, data = Bundle().apply {
                    putInt(ARouterMap.IMG_INDEX, 1)
                    putStringArrayList(ARouterMap.IMG_LIST, picList as ArrayList<String>)
                })
            }

            R.id.riv_pic3 -> {
                startArouter(ARouterMap.PREVIEW_IMAGE, data = Bundle().apply {
                    putInt(ARouterMap.IMG_INDEX, 2)
                    putStringArrayList(ARouterMap.IMG_LIST, picList as ArrayList<String>)
                })
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            picInquiryLiveData.observe(this@OrderDetailActivity) {
                dismissProgressDialog()
                picList.add(it.inPicture11)
                picList.add(it.inPicture10)
                picList.add(it.inPicture20)
                GlideUtils.instance?.loadImage(binding.rivPic1, picList[0], com.rt.common.R.mipmap.ic_placeholder)
                GlideUtils.instance?.loadImage(binding.rivPic2, picList[1], com.rt.common.R.mipmap.ic_placeholder)
                GlideUtils.instance?.loadImage(binding.rivPic3, picList[2], com.rt.common.R.mipmap.ic_placeholder)
            }
            debtUploadLiveData.observe(this@OrderDetailActivity) {
                dismissProgressDialog()
                if (it.result) {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.上传成功))
                    EventBus.getDefault().post(RefreshOrderListEvent())
                    binding.rtvUpload.delegate.setBackgroundColor(
                        ContextCompat.getColor(
                            BaseApplication.instance(), com.rt.base.R.color.color_ffc5dddb
                        )
                    )
                    binding.rtvUpload.setOnClickListener(null)
                    binding.rtvUpload.delegate.init()
                } else {
                    ToastUtil.showMiddleToast(i18N(com.rt.base.R.string.上传失败))
                }
            }
            errMsg.observe(this@OrderDetailActivity) {
                dismissProgressDialog()
                ToastUtil.showMiddleToast(it.msg)
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }

    override fun providerVMClass(): Class<OrderDetailViewModel> {
        return OrderDetailViewModel::class.java
    }
}