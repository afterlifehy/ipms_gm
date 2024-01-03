package com.rt.base.ext

import androidx.fragment.app.Fragment

fun Fragment.i18N(id: Int): String {
    return resources.getString(id)
}
