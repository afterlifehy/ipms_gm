package com.rt.common.help

import android.content.Context
import com.rt.base.view.MyRefreshHeader
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator

object SmartRefreshHelp {
    /**
     * 初始化全局的加载效果
     */
    fun initRefHead() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(object : DefaultRefreshHeaderCreator {
            override fun createRefreshHeader(
                context: Context,
                layout: RefreshLayout
            ): RefreshHeader {
//                return MyRefreshHeader(context)
                val header = ClassicsHeader(context).setDrawableSize(15f)
                header.setTextSizeTitle(12f)
                header.setFinishDuration(0)
                return header
            }
        })
        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {
            override fun createRefreshFooter(
                context: Context,
                layout: RefreshLayout
            ): RefreshFooter {
                //指定为经典Footer，默认是 BallPulseFooter
                val footer = ClassicsFooter(context).setDrawableSize(15f)
                footer.setTextSizeTitle(12f)
                footer.setFinishDuration(0)
                return footer
            }
        })
    }
}