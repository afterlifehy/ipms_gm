package com.rt.base.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginBean(
    var name: String,
    var loginName: String,
    var phone: String,
    var result: List<Street>,
    var simId: String
):Parcelable