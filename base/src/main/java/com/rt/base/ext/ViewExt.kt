package com.rt.base.ext

import android.view.View

fun View.gone(): View{
    this.visibility = View.GONE
    return this
}

fun View.show(): View{
    this.visibility = View.VISIBLE
    return this
}

fun View.hide(): View{
    this.visibility = View.INVISIBLE
    return this
}
