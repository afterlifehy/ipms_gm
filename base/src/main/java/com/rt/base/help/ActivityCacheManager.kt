package com.rt.base.help

import android.app.Activity
import java.util.*
import kotlin.system.exitProcess

/**
 * Created by huy  on 2022/8/3.
 */
class ActivityCacheManager {
    private var activities: Stack<Activity>? = null

    companion object {
        private var _instance: ActivityCacheManager? = null
        fun instance(): ActivityCacheManager {
            if (_instance == null) {
                _instance = ActivityCacheManager()
            }
            return _instance!!
        }
    }

    fun addActivity(activity: Activity?) {
        if (activities == null) {
            activities = Stack()
        }
        if (activities!!.search(activity) === -1) {
            activities!!.push(activity)
        }
    }

    fun removeActivity(activity: Activity?) {
        if (activities != null && activities!!.size > 0 && activities!!.search(activity) !== -1) {
            activities!!.remove(activity)
        }
    }

    fun exit() {
        if (activities != null && activities!!.size > 0) {
            while (!activities!!.empty()) {
                val mActivity = activities!!.pop()
                mActivity?.finish()
            }
        }
        exitProcess(0)
    }

    //得到当前activity
    fun getCurrentActivity(): Activity? {
        return activities?.lastElement()
    }

    fun getAllActivity(): List<Activity> {
        return activities!!
    }
}