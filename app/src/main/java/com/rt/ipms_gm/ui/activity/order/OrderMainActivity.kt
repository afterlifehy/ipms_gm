package com.rt.ipms_gm.ui.activity.order

import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.ext.gone
import com.rt.base.ext.i18N
import com.rt.base.ext.show
import com.rt.base.ext.startArouter
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_gm.R
import com.rt.ipms_gm.databinding.ActivityOrderMainBinding
import com.rt.ipms_gm.mvvm.viewmodel.OrderMainViewmodel

@Route(path = ARouterMap.ORDER_MAIN)
class OrderMainActivity : VbBaseActivity<OrderMainViewmodel, ActivityOrderMainBinding>(), OnClickListener {

    override fun initView() {
        binding.layoutToolbar.flBack.gone()
        binding.layoutToolbar.ivBackHome.show()
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.订单)
        binding.layoutToolbar.tvTitle.setTextColor(
            ContextCompat.getColor(
                BaseApplication.baseApplication,
                com.rt.base.R.color.white
            )
        )
    }

    override fun initListener() {
        binding.layoutToolbar.ivBackHome.setOnClickListener(this)
        binding.rflTransactionQuery.setOnClickListener(this)
        binding.rflDebtCollect.setOnClickListener(this)
        binding.rflOrderInquiry.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_backHome -> {
                onBackPressedSupport()
            }

            R.id.rfl_transactionQuery -> {
                startArouter(ARouterMap.TRANSACTION_INQUIRY)
            }

            R.id.rfl_debtCollect -> {
                startArouter(ARouterMap.DEBT_COLLECTION)
            }

            R.id.rfl_orderInquiry -> {
                startArouter(ARouterMap.ORDER_INQUIRY)
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityOrderMainBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun providerVMClass(): Class<OrderMainViewmodel> {
        return OrderMainViewmodel::class.java
    }

    override fun marginStatusBarView(): View {
        return binding.layoutToolbar.ablToolbar
    }

}