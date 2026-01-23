package com.rt.base.viewbase

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.rt.base.R
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.dialog.IOSLoadingDialog
import com.rt.base.event.BaseEvent
import me.yokeyword.fragmentation.ISupportActivity
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseActivity<VM : BaseViewModel> : SupportActivity(), ISupportActivity{
    protected lateinit var mViewModel: VM
    private var mFragment: Fragment? = null
    private var isLoadContentView = true

    private lateinit var mProgressDialog: IOSLoadingDialog
    var loadingView: View? = null
    val log: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(baseEvent: BaseEvent) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log.info("-------------${javaClass.simpleName} CREATE--------------------------")
        if (!isHorizontalScreen()) {//设置智能竖屏
            try {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.onCreate(savedInstanceState)

        val loadBuilder = IOSLoadingDialog.Builder(this)
            .setMessage("Loading")
            .setShowMessage(false)
            .setCancelable(true)
            .setCancelOutside(false)
        mProgressDialog = loadBuilder.create()

        if (isLoadContentView) {
            setContentView(getLayoutResId())
        }
        initVM()
        initView()
        initListener()
        initData()
        startObserve()
        if (isRegEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    fun initVM() {
        providerVMClass()?.let {
            mViewModel = ViewModelProvider(this).get(it)
            mViewModel.let(lifecycle::addObserver)
        }
    }

    /**
     * 是否可以横屏，默认是竖屏
     */
    open fun isHorizontalScreen(): Boolean {
        return false
    }

    /**
     * 设置这个类里面是否加载setContentView
     */
    protected fun setIsLoadContentView(isLoadContentView: Boolean) {
        this.isLoadContentView = isLoadContentView
    }

    fun getActivity(): Activity {
        return this
    }

    fun setStatusBarColor() {
        setStatusBarColor(R.color.black, false)
    }

    fun setStatusBarColor(color: Int, isLightMode: Boolean) {
        //设置状态栏为白底黑字
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, color))
        BarUtils.setStatusBarLightMode(this, isLightMode)
    }

    /**
     * 添加
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected open fun addFragment(frameLayoutId: Int, fragment: Fragment?) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            if (fragment.isAdded) {
                if (mFragment != null) {
                    transaction.hide(mFragment!!).show(fragment)
                } else {
                    transaction.show(fragment)
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment!!).add(frameLayoutId, fragment)
                } else {
                    transaction.add(frameLayoutId, fragment)
                }
            }
            mFragment = fragment
            transaction.commit()
        }
    }

    fun showProgressDialog(i: Long) {
//        mProgressDialog.show()
        runOnUiThread {
            loadingView?.let {
                it.visibility = View.VISIBLE
            } ?: run {
                val view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null, false)
                loadingView = view.findViewById(R.id.flLoading)
                window.addContentView(loadingView,
                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                        gravity = Gravity.CENTER
                    }
                )
                loadingView?.visibility = View.VISIBLE
                loadingView?.bringToFront()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({ dismissProgressDialog() }, i)
    }

    fun dismissProgressDialog() {
//        mProgressDialog.dismiss()
        runOnUiThread {
            loadingView?.let {
                it.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        log.info("-------------${javaClass.simpleName} RESUME--------------------------")
    }

    override fun onPause() {
        super.onPause()
        log.info("-------------${javaClass.simpleName} PAUSE--------------------------")
    }

    override fun onDestroy() {
        log.info("-------------${javaClass.simpleName} DESTROY--------------------------")
        if (::mViewModel.isInitialized) {
            mViewModel.let {
                lifecycle.removeObserver(it)
            }
        }
        if (isRegEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    open fun providerVMClass(): Class<VM>? = null
    open fun startObserve() {}
    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
    open fun isRegEventBus(): Boolean {
        return false
    }

}