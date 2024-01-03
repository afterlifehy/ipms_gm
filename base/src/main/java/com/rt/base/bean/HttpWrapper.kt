package com.rt.base.bean

data class HttpWrapper<out T>(val msg: String, val status: Int, val attr: T)