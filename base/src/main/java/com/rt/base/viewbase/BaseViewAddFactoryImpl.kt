package com.rt.base.viewbase

import android.content.Context
import android.view.View
import com.rt.base.R
import com.rt.base.widget.NoDataView

class BaseViewAddFactoryImpl : BaseViewAddFactory {


    override fun getRoootView(context: Context): View {//写在这里好统一布局
        val mRootView = View.inflate(context, R.layout.base_no_title_layout, null)
        return mRootView
    }


    override fun getNotDataView(context: Context): View {
        //val mAddView = View.inflate(context, R.layout.not_data_layout, null)
        return NoDataView(context)
    }

    override fun getNewWorkErrorView(context: Context): View {
        val mAddView = View.inflate(context, R.layout.network_error_data_layout, null)
        return mAddView
    }

    override fun getLoadProgressView(context: Context): View {
        val mAddView = View.inflate(context, R.layout.data_load_layout, null)
        return mAddView
    }
}