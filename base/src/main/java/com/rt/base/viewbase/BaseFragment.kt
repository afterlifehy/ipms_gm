package com.rt.base.viewbase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rt.base.base.mvvm.BaseViewModel
import com.rt.base.base.mvvm.OnNetWorkCallLinsener
import com.rt.base.bean.NetWorkRequestData
import com.rt.base.dialog.IOSLoadingDialog
import com.rt.base.network.NetWorkMonitorManager
import com.rt.base.network.NetWorkState
import com.rt.base.network.ViewNetWorkStateManager
import com.rt.base.viewbase.inter.NetWorkRequestLinsener
import com.rt.base.viewbase.inter.OnNetWorkViewShowLinsener
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

abstract class BaseFragment<VM : BaseViewModel> : Fragment(),
    OnNetWorkViewShowLinsener, NetWorkRequestLinsener, OnNetWorkCallLinsener {
    protected lateinit var mViewModel: VM
    var mRoot: View? = null
    var mInflater: LayoutInflater? = null
    private var mFragment: Fragment? = null
    private var mNewWorkStateManager: ViewNetWorkStateManager? = null

    //用来存储需要监听的网络错误
    private var networkErrorTagList = ArrayList<String>()
    private lateinit var mProgressDialog: IOSLoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loadBuilder =
            IOSLoadingDialog.Builder(activity)
                .setMessage("")
                .setShowMessage(false)
                .setCancelable(true)
                .setCancelOutside(false)
        mProgressDialog = loadBuilder.create()

        mNewWorkStateManager = ViewNetWorkStateManager(this, false)
        lifecycle.addObserver(mNewWorkStateManager!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initVM()
        initView()
        initListener()
        initData()
        startObserve()
        if (!EventBus.getDefault().isRegistered(this) && isRegEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), null)
    }

    private fun initVM() {
        providerVMClass()?.let {
            mViewModel = ViewModelProvider(this).get(it)
            mViewModel.let(lifecycle::addObserver)
            mViewModel.regNetWorkRequestLinsener(this)
        }
    }

    open fun isRegEventBus(): Boolean {
        return false
    }

    fun onRestartInstance(bundle: Bundle) {

    }

    protected fun startActivity(z: Class<*>) {
        startActivity(Intent(activity, z))
    }

    fun showProgressDialog() {
        mProgressDialog.show()
    }

    fun dismissProgressDialog() {
        mProgressDialog.dismiss()
    }

    /**
     * 添加
     *
     * @param frameLayoutId
     * @param fragment
     */
    protected open fun addFragment(frameLayoutId: Int, fragment: Fragment?) {
        if (fragment != null) {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (fragment.isAdded) {
                if (mFragment != null) {
                    transaction?.hide(mFragment!!)?.show(fragment)
                } else {
                    transaction?.show(fragment)
                }
            } else {
                if (mFragment != null) {
                    transaction?.hide(mFragment!!)?.add(frameLayoutId, fragment)
                } else {
                    transaction?.add(frameLayoutId, fragment)
                }
            }
            mFragment = fragment
            transaction?.commit()
        }
    }

    /**
     * 如果需在要当前界面知道是否有网络，就可以实现这个类
     */
    open fun currentNetWorkState(isNetWork: Boolean) {

    }

    override fun onCurrentNewWorkState(isNetWork: Boolean) {
        currentNetWorkState(isNetWork)
    }

    /**
     * 需要响应调用方法出现网络错误时候，需要添加一个
     */
    fun addNetWorkErrorTag(tag: String) {
        networkErrorTagList.add(tag)
    }


    override fun onNewWorkErrorCall(tag: String, ext: Exception?) {
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
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    open fun startObserve() {}
    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
    open fun providerVMClass(): Class<VM>? = null
}