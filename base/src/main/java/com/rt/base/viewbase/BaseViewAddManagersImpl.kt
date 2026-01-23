package com.rt.base.viewbase

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.rt.base.R
import com.rt.base.widget.PagerStatesView

class BaseViewAddManagersImpl : BaseViewAddManagers {
    private var mRootView: View? = null
    protected var mViewAddManager: BaseViewAddFactory? = null
    private var mContext: Context? = null

    init {
        mViewAddManager = BaseViewAddFactory.getInstance()
    }

    override fun getRootView(context: Context, mView: View): View {
        mContext = context
        mRootView = mViewAddManager?.getRootView(context)!!
        val fl_conent = mRootView!!.findViewById<FrameLayout>(R.id.fl_conent)
        fl_conent.addView(mView)
        return mRootView!!
    }

    override fun getRootViewId(context: Context, contextId: Int): View {
        mContext = context
        mRootView = mViewAddManager?.getRootView(context)!!
        addContentView(mRootView, contextId)
        return mRootView!!
    }

    /**
     * 把子布局添加进来
     */
    private fun addContentView(view: View?, contentId: Int) {
        val fl_conent = view!!.findViewById<FrameLayout>(R.id.fl_conent)
        val mContetxView = View.inflate(view.context, contentId, null)
        fl_conent.addView(mContetxView)
    }
}