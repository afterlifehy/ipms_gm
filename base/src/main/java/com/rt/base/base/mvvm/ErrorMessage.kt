package com.rt.base.base.mvvm

data class ErrorMessage(
    var msg: String,
    var type: String = "",
    var code: Int = 0,
    var data: Any? = null
)