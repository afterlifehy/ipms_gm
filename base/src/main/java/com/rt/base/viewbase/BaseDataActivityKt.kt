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
import com.rt.base.base.mvvm.BaseViewModel


abstract class BaseDataActivityKt<VM : BaseViewModel> : BaseActivity<VM>(), View.OnTouchListener{
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
        setStatusBarColor(R.color.black, true)

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
        return R.color.black
    }

    open fun marginStatusBarView(): View? {
        return null
    }

    abstract val isFullScreen: Boolean

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
}