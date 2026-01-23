package com.rt.ipms_mg.ui.activity

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.rt.base.BaseApplication
import com.rt.base.ds.PreferencesDataStore
import com.rt.base.ds.PreferencesKeys
import com.rt.base.start.StartUpKey
import com.rt.base.viewbase.VbBaseActivity
import com.rt.ipms_mg.databinding.ActivitySplashBinding
import com.rt.ipms_mg.mvvm.viewmodel.SplashViewModel
import com.rt.ipms_mg.startup.ApplicationAnchorTaskCreator
import com.xj.anchortask.library.AnchorProject
import com.xj.anchortask.library.OnProjectExecuteListener
import com.xj.anchortask.library.log.LogUtils
import com.rt.base.ext.startAct
import com.rt.ipms_mg.ui.activity.login.LoginActivity
import com.rt.common.realm.RealmUtil
import kotlinx.coroutines.runBlocking

class SplashActivity : VbBaseActivity<SplashViewModel, ActivitySplashBinding>(),
    OnProjectExecuteListener {
    var project: AnchorProject? = null

    override fun initView() {
        BarUtils.setStatusBarColor(
            this,
            ContextCompat.getColor(this, com.rt.base.R.color.transparent)
        )
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        setCustomDensity()
    }

    fun setCustomDensity() {
        val appDisplayMetrics: DisplayMetrics = application.resources.displayMetrics

        val targetDensity = appDisplayMetrics.heightPixels / 640f
        val targetDensityDpi = (160 * targetDensity).toInt()
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        val activityDisplayMetrics = resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    override fun initListener() {
    }

    override fun initData() {
        project = AnchorProject.Builder().setContext(this).setLogLevel(LogUtils.LogLevel.DEBUG)
            .setAnchorTaskCreator(ApplicationAnchorTaskCreator())
            .addTask(StartUpKey.TASK_NAME_ONE)
            .addTask(StartUpKey.TASK_NAME_TWO).afterTask(StartUpKey.TASK_NAME_ONE)
            .build()
        project?.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        project?.start()
    }

    override fun getVbBindingView(): ViewBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override val isFullScreen: Boolean
        get() = true

    override fun providerVMClass(): Class<SplashViewModel> {
        return SplashViewModel::class.java
    }

    override fun onProjectFinish() {
        runBlocking {
            PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.simId, "")
            PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.phone, "")
            PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.name, "")
            PreferencesDataStore(BaseApplication.instance()).putString(PreferencesKeys.loginName, "")
        }
        RealmUtil.instance?.deleteAllStreet()
        Handler(Looper.getMainLooper()).postDelayed({
            runBlocking {
                if (PreferencesDataStore(BaseApplication.instance()).getString(PreferencesKeys.simId).isEmpty()) {
                    startAct<LoginActivity>()
                } else {
                    startAct<MainActivity>()
                }
                finish()
            }
        }, 100)
    }

    override fun onProjectStart() {
    }

    override fun onTaskFinish(taskName: String) {
    }
}