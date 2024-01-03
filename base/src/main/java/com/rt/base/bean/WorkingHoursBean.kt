package com.rt.base.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WorkingHoursBean(
    @PrimaryKey
    var loginName: String = "",
    var time: Long = 0L
) : RealmObject() {
}
