package com.rt.base.bean

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class Street(
    @PrimaryKey
    var streetNo: String = "",
    var streetName: String = "",
    var ischeck: Boolean = false,
    var isCurrent: Boolean = false
) : RealmObject(), Parcelable {
    fun copy(): Street {
        return Street(streetNo, streetName, ischeck, isCurrent)
    }
}
