package com.rt.base.viewbase

import android.app.Activity
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.rt.base.R
import com.rt.base.widget.PagerStatesView

class BaseViewAddManagersImpl : BaseViewAddManagers {
    //数据加载匡
    private var loadData: View? = null

    //加载动画
    private var mLadAnimation: AnimationDrawable? = null
    private var loading_iv //动画加载img
            : ImageView? = null

    //是否加载子布局
    private var isLoadNotData = false

    //是否显示title
    private var isShowTitle = false
    private var mRootView: View? = null
    protected var mViewAddManager: BaseViewAddFactory? = null
    private var mContext: Context? = null
    private var fl_other_content: PagerStatesView? = null

    //title
    private var tv_base_title: TextView? = null

    init {
        mViewAddManager = BaseViewAddFactory.getInsten()
    }

    override fun getRootView(context: Context, mView: View): View {
        mContext = context
        mRootView = mViewAddManager?.getRoootView(context)!!
        fl_other_content = mRootView?.findViewById(R.id.fl_other_content)
        val fl_conent = mRootView!!.findViewById<FrameLayout>(R.id.fl_conent)
        fl_conent.addView(mView)
        return mRootView!!
    }

    override fun getRootViewId(context: Context, contextId: Int): View {
        mContext = context
        mRootView = mViewAddManager?.getRoootView(context)!!
        fl_other_content = mRootView?.findViewById(R.id.fl_other_content)
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

    override fun setIsLoadNotData(isLoadNotDataEmp: Boolean) {
        isLoadNotData = isLoadNotDataEmp
        if (isLoadNotDataEmp) {
            mRootView?.apply {
                loadData = mViewAddManager?.getLoadProgressView(context)
            }

        }
    }

    private fun initTitle() {
        mRootView?.apply {
            findViewById<Toolbar>(R.id.toolbar_navigation)?.setNavigationOnClickListener {
                if (context is Activity) {
                    (context as Activity).onBackPressed()
                } else if (context is Fragment) {
                    (context as Fragment).activity?.onBackPressed()
                }
            }
        }
    }

    override fun setIsShowTitle(isShowTitleEmp: Boolean) {
        isShowTitle = isShowTitleEmp
        if (isShowTitleEmp) {//显示title
            mRootView?.findViewById<View>(R.id.base_title)?.visibility = View.VISIBLE
            tv_base_title = mRootView?.findViewById(R.id.tv_base_title)
            initTitle()
        }

    }

    override fun setNetWorkStatesView(
        isShow: Boolean,
        mOnNotNetWorkClickLinsener: PagerStatesView.OnPagerClickLinsener?
    ) {
        fl_other_content?.addErrorNetWorkView(isShow, mOnNotNetWorkClickLinsener)
    }

    override fun setTitleName(title: String) {
        tv_base_title?.setText(title)
    }

    override fun showPromptView(
        text: String,
        errorIcont: Int,
        mOnNotNetWorkClickLinsener: PagerStatesView.OnPagerClickLinsener?
    ) {
        fl_other_content?.addNotDataView(true, errorIcont, text, mOnNotNetWorkClickLinsener)
    }

    override fun hidePromptView() {
        fl_other_content?.addNotDataView(false)
    }

    override fun showLoadData() {
        fl_other_content?.addLoadProgress(true)
    }

    override fun onMyPause() {
        stiopAnimation()
    }

    override fun hideLoadData() {
        fl_other_content?.addLoadProgress(false)
    }

    override fun showNewWorkError(mOnNotNetWorkClickLinsener: PagerStatesView.OnPagerClickLinsener?) {
        fl_other_content?.addErrorNetWorkView(true, mOnNotNetWorkClickLinsener)
    }

    override fun goneNewWorkError() {
        fl_other_content?.addErrorNetWorkView(false)

    }

    override fun setRightIcon(icon: Int, onCLickLinsenr: View.OnClickListener) {

        mRootView?.findViewById<TextView>(R.id.seller_collection_iv)?.apply {
            visibility = View.VISIBLE
            setBackgroundResource(icon)
            setOnClickListener(onCLickLinsenr)
        }

    }


    override fun notDataIsVisib(): Boolean {
        return false
    }

    /**
     * 停止动画
     */
    private fun stiopAnimation() {
        mLadAnimation?.apply {
            if (isRunning) {
                stop()
            }
        }
    }

}