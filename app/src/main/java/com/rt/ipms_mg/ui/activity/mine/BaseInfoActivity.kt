package com.rt.ipms_mg.ui.activity.mine

import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.rt.base.BaseApplication
import com.rt.base.arouter.ARouterMap
import com.rt.base.bean.Street
import com.rt.base.ext.i18N
import com.rt.base.viewbase.VbBaseActivity
import com.rt.common.realm.RealmUtil
import com.rt.common.util.GlideUtils
import com.rt.ipms_mg.R
import com.rt.ipms_mg.databinding.ActivityBaseInfoBinding
import com.rt.ipms_mg.mvvm.viewmodel.BaseInfoViewModel

@Route(path = ARouterMap.BASE_INFO)
class BaseInfoActivity : VbBaseActivity<BaseInfoViewModel, ActivityBaseInfoBinding>(), OnClickListener {
    var streetList: MutableList<Street> = ArrayList()
    override fun initView() {
        GlideUtils.instance?.loadImage(binding.layoutToolbar.ivBack, com.rt.common.R.mipmap.ic_back_white)
        binding.layoutToolbar.tvTitle.text = i18N(com.rt.base.R.string.基本信息)
        binding.layoutToolbar.tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.instance(), com.rt.base.R.color.white))

    }

    override fun initListener() {
        binding.layoutToolbar.flBack.setOnClickListener(this)
    }

    override fun initData() {
        streetList = RealmUtil.instance?.findCheckedStreetList() as MutableList<Street>
        for (i in streetList) {
            val street = View.inflate(BaseApplication.instance(), R.layout.item_baseinfo_street, null)
            street.findViewById<TextView>(R.id.tv_street).text = i.streetName
            binding.llStreet.addView(street)
        }
        mViewModel.getBaseInfo()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                onBackPressedSupport()
            }
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            baseInfoLiveData.observe(this@BaseInfoActivity) {
                binding.tvName.text = it[0]
                binding.tvAccount.text = it[1]
                binding.tvPhoneNum.text = it[2]
            }
        }
    }

    override fun providerVMClass(): Class<BaseInfoViewModel>? {
        return BaseInfoViewModel::class.java
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityBaseInfoBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun marginStatusBarView(): View? {
        return binding.layoutToolbar.ablToolbar
    }
}