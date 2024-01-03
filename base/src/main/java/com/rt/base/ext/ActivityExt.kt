package com.rt.base.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.rt.base.help.ActivityCacheManager


fun Activity.i18N(id: Int): String {
    return this.resources.getString(id)
}

fun Activity.isEmpty(value: String): Boolean {
    return TextUtils.isEmpty(value)
}

inline fun <reified T : Activity> Context.startAct(data: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (data != null) {
        intent.putExtras(data)
    }

    this.startActivity(intent)
}

inline fun startArouter(path: String, data: Bundle? = null) {
    ARouter.getInstance().build(path).with(data).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
}

