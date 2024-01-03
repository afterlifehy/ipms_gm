package com.rt.base.viewbase


import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.rt.base.R
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.OnNetWorkCallLinsener
import com.rt.base.bean.NetWorkRequestData
import com.rt.base.dialog.IOSLoadingDialog
import com.rt.base.event.BaseEvent
import com.rt.base.network.NetWorkMonitorManager
import com.rt.base.network.NetWorkState
import com.rt.base.network.ViewNetWorkStateManager
import com.rt.base.viewbase.inter.NetWorkRequestLinsener
import com.rt.base.viewbase.inter.OnNetWorkViewShowLinsener
import me.yokeyword.fragmentation.ISupportActivity
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity<VM : BaseViewModel> : SupportActivity(), ISupportActivity,
    OnNetWorkViewShowLinsener, NetWorkRequestLinsener,
    OnNetWorkCallLinsener {
    protected lateinit var mViewModel: VM
    private var mFragment: Fragment? = null
    private var isLoadContentView = true

    private var mNewWorkStateManager: ViewNetWorkStateManager? = null

    //用来存储需要监听的网络错误
    private var networkErrorTagList = ArrayList<String>()
    private lateinit var mProgressDialog: IOSLoadingDialog

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(baseEvent: BaseEvent) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
        mNewWorkStateManager = ViewNetWorkStateManager(this, true)
        lifecycle.addObserver(mNewWorkStateManager!!)
    }

    fun initVM() {
        providerVMClass()?.let {
            mViewModel = ViewModelProvider(this).get(it)
            mViewModel.let(lifecycle::addObserver)
            mViewModel.regNetWorkRequestLinsener(this)
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
        mProgressDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({ dismissProgressDialog() }, i)
    }

    fun dismissProgressDialog() {
        mProgressDialog.dismiss()
    }

    /**
     * 需要响应调用方法出现网络错误时候，需要添加一个
     */
    fun addNetWorkErrorTag(tag: String) {
        networkErrorTagList.add(tag)
    }

    /**
     * 如果需在要当前界面知道是否有网络，就可以实现这个类
     */
    open fun currentNewWorkState(isNetWork: Boolean) {

    }

    override fun onCurrentNewWorkState(isNetWork: Boolean) {
        currentNewWorkState(isNetWork)
    }

    override fun onNewWorkErrorCall(tag: String, ext: java.lang.Exception?) {
        if (networkErrorTagList.contains(tag)) {
            val info = NetWorkRequestData(1, ext?.message!!, tag)
            if (NetWorkMonitorManager.getInstance().currNetWorkState == NetWorkState.NONE) {
                onNoNetWorkErrror(info)
            } else {
                onNetWorkRequestError(info)
            }
        }
    }

    override fun onDestroy() {
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