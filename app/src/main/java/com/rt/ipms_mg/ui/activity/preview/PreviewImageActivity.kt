package com.rt.ipms_mg.ui.activity.preview

import android.Manifest
import android.annotation.SuppressLint
import android.view.View
import android.view.View.OnClickListener
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.rt.base.arouter.ARouterMap
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_mg.adapter.SamplePagerAdapter
import com.rt.ipms_mg.databinding.ActivityPreviewImageBinding
import com.rt.ipms_mg.mvvm.viewmodel.PreviewImageViewModel
import com.tbruyelle.rxpermissions3.RxPermissions
import com.rt.ipms_mg.R
import java.util.ArrayList

@Route(path = ARouterMap.PREVIEW_IMAGE)
class PreviewImageActivity : VbBaseActivity<PreviewImageViewModel, ActivityPreviewImageBinding>(), OnClickListener {
    var index = 0
    var imageList: ArrayList<String> = ArrayList()
    var samplePagerAdapter: SamplePagerAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun initView() {
        index = intent.getIntExtra(ARouterMap.IMG_INDEX, 0)
        imageList = intent.getStringArrayListExtra(ARouterMap.IMG_LIST) as ArrayList<String>

        samplePagerAdapter = SamplePagerAdapter(this, imageList)
        binding.hvpViewpager.adapter = samplePagerAdapter
        binding.hvpViewpager.currentItem = index
        binding.hvpViewpager.offscreenPageLimit = imageList.size
        binding.rtvIndicator.text = "${(index + 1)}/${imageList.size}"

        binding.hvpViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.rtvIndicator.text = "${(position + 1)}/${imageList.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        binding.hvpViewpager.setOnClickListener(this)

        var rxPermissions = RxPermissions(this@PreviewImageActivity)
        rxPermissions.request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .subscribe {
                if (it) {
                } else {

                }
            }
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.pv_img -> {
                onBackPressedSupport()
            }
        }
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivityPreviewImageBinding.inflate(layoutInflater)
    }

    override fun onReloadData() {
    }

    override val isFullScreen: Boolean
        get() = true

    override fun providerVMClass(): Class<PreviewImageViewModel>? {
        return PreviewImageViewModel::class.java
    }
}