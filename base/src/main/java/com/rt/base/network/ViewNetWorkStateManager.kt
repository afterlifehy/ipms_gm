package com.rt.base.network

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.LifecycleObserver
import com.rt.base.viewbase.inter.OnNetWorkViewShowLinsener

class ViewNetWorkStateManager(
    mOnNetWorkViewShowLinsener: OnNetWorkViewShowLinsener,
    isActivity: Boolean
) :
    LifecycleObserver {
    private var mOnNetWorkViewShowLinsener: OnNetWorkViewShowLinsener? = null

    /**
     * 当前界面是activity还是fragmetn, true为activity ,false为fragment
     */
    private var isActivity = true
    private var mHandler: Handler? = null

    init {
        this.isActivity = isActivity
        this.mOnNetWorkViewShowLinsener = mOnNetWorkViewShowLinsener
        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                mOnNetWorkViewShowLinsener.onCurrentNewWorkState(msg.obj as Boolean)
            }
        }
    }

    fun onCraete() {
        NetWorkMonitorManager.getInstance().register(this)
    }

    fun onResume() {
    }

    fun onPause() {

    }

    fun onDestroy() {
        NetWorkMonitorManager.getInstance().unregister(this)
        mHandler = null
    }

    fun onNetWorkStateChange(netWorkState: NetWorkState) {
        val mMsg = mHandler?.obtainMessage()
        mMsg?.obj = netWorkState != NetWorkState.NONE
        mHandler?.sendMessage(mMsg!!)
    }
}