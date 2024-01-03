package com.rt.base.viewbase

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blankj.utilcode.util.BarUtils
import com.rt.base.R
import com.rt.base.bean.NetWorkRequestData
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.widget.PagerStatesView


abstract class BaseDataActivityKt<VM : BaseViewModel> : BaseActivity<VM>(), View.OnTouchListener,
    BaseCommentLinsener, PagerStatesView.OnPagerClickLinsener {
    protected var mRoot: View? = null
    protected var mViewAddManager: BaseViewAddManagers? = null

    init {
        mViewAddManager = BaseViewAddManagersImpl()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val mBinView = getBindingView()
        if (mBinView == null) {
            mRoot = mViewAddManager?.getRootViewId(this, getLayoutResId())!!
        } else {
            mRoot = mViewAddManager?.getRootView(this, mBinView)!!

        }
        mViewAddManager?.setIsShowTitle(isShowTitle())
        mViewAddManager?.setIsLoadNotData(isLoadNotData())

        setStatusBarColor(com.rt.base.R.color.black, false)
        if (isFullScreen) {
            BarUtils.transparentStatusBar(this)
            if (marginStatusBarView() != null) {
                BarUtils.addMarginTopEqualStatusBarHeight(marginStatusBarView()!!)
                marginStatusBarView()?.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseDataActivityKt,
                        R.color.transparent
                    )
                )
            }
        }
        BarUtils.setNavBarColor(this, ContextCompat.getColor(this, navbarColor()))

        savedInstanceState?.let { }
        setIsLoadContentView(false)
        setContentView(mRoot)
        super.onCreate(savedInstanceState)
    }

    open fun getBindingView(): View? {
        return null
    }

    open fun navbarColor(): Int {
        return R.color.white
    }

    open fun marginStatusBarView(): View? {
        return null
    }

    protected abstract fun onReloadData()

    /**
     * 是否需要添加暂无数据
     *
     * @return
     */
    open fun isLoadNotData(): Boolean {
        return false
    }

    /**
     * 是否需要显示title
     */
    open fun isShowTitle(): Boolean {
        return false
    }

    abstract val isFullScreen: Boolean

    /**
     * 如果有分页，统一关闭一些加载效果
     */
    open fun hideLoadStatus() {
        hideLoadData()
    }

    /**
     * 设置左边控件
     */
    override fun setRightIcon(icon: Int, onCLickLinsenr: View.OnClickListener) {
        mViewAddManager?.setRightIcon(icon, onCLickLinsenr)
    }

    /**
     * 判断当前是否暂无数据了
     *
     * @return
     */
    override fun notDataIsVisib(): Boolean {
        return mViewAddManager?.notDataIsVisib()!!
    }


    /**
     * @param textName
     */
    override fun setTitleName(title: String) {
        mViewAddManager?.setTitleName(title)
    }

    /**
     * 默认图标
     */
    override fun showPromptView(
        text: String,
        errorIcont: Int,
        mOnNotNetWorkClickLinsener: PagerStatesView.OnPagerClickLinsener?
    ) {
        mViewAddManager?.showPromptView(
            text,
            errorIcont,
            mOnNotNetWorkClickLinsener
        )

    }

    fun showNotDataView() {
    }

    /**
     * 显示加载失败，点击屏幕重试
     */
    fun showLoadError() {
    }

    /**
     * 请求数据
     */
    open fun getData() {

    }

    /**
     * 隐藏我的
     */
    override fun hidePromptView() {
        mViewAddManager?.hidePromptView()
    }

    /**
     * 暂无网络
     */
    override fun showNewWorkError(mOnNotNetWorkClickLinsener: PagerStatesView.OnPagerClickLinsener?) {
        mViewAddManager?.showNewWorkError(this)
    }

    /**
     * 隐藏掉暂无网络
     */
    override fun goneNewWorkError() {
        mViewAddManager?.goneNewWorkError()
    }


    /**
     * 加载动画
     */
    override fun showLoadData() {
        mViewAddManager?.showLoadData()
    }


    override fun onPause() {
        super.onPause()
        onMyPause()
    }

    /**
     * 隐藏加载框A
     */
    override fun hideLoadData() {
        mViewAddManager?.hideLoadData()
    }

    override fun onMyPause() {
        mViewAddManager?.onMyPause()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return checkTouch()

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return checkTouch()
    }

    fun checkTouch(): Boolean {
        onMyTouch()
        if (null != currentFocus) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            val mInputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        return false
    }

    /**
     * 点击了布局
     */
    fun onMyTouch() { //点击其他地方的时候，EditText失去焦点
        mRoot?.isFocusable = true
        mRoot?.isFocusableInTouchMode = true
        mRoot?.requestFocus()
    }

    protected fun getFragment(tag: String?): Fragment? {
        val fragmentManager = supportFragmentManager ?: return null
        val fragment = fragmentManager.findFragmentByTag(tag)
        return if (fragment is Fragment) fragment else null
    }

    fun showFragment(
        fragmentManager: FragmentManager,
        fragmentTransaction: FragmentTransaction?,
        willShowFragment: Fragment?,
        id: Int,
        tag: String
    ) {
        var fragmentTransaction = fragmentTransaction
        fragmentTransaction = fragmentManager.beginTransaction()
        if (willShowFragment == null) {
            return
        }
        hideFragments(fragmentManager, fragmentTransaction)
        if (!willShowFragment.isAdded && null == fragmentManager.findFragmentByTag(tag)) {
            fragmentTransaction.add(id, willShowFragment, tag)
        } else {
            fragmentTransaction.show(willShowFragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun hideFragments(
        fragmentManager: FragmentManager,
        fragmentTransaction: FragmentTransaction
    ) {
        val fragments = fragmentManager.fragments
        for (i in fragments.indices) {
            if (fragments[i] != null && fragments[i].isAdded) {
                fragmentTransaction.hide(fragments[i])
            }
        }
    }

    override fun onNetWorkRequestError(errror: NetWorkRequestData) {
        showLoadError()
    }

    override fun onNoNetWorkErrror(errror: NetWorkRequestData) {
        showNewWorkError()
    }

    override fun onPagerClick() {
        onReloadData()
        getData()
        hidePromptView()
    }
}