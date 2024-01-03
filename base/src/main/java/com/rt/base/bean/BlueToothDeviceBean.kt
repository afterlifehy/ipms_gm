package com.rt.base.bean

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

open class BlueToothDeviceBean(
    @PrimaryKey
    var address: String = "",
    var name: String = ""
) : RealmObject() {
}
