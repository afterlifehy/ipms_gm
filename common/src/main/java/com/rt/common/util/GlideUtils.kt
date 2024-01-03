package com.rt.common.util

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.rt.base.BaseApplication
import com.rt.common.R
import java.io.File

class GlideUtils private constructor() {
    /**
     * 初始化一个默认的配置
     */
    private fun initOption() {
        defaultOption = RequestOptions()
        defaultOption!!.error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
        /*
            DiskCacheStrategy.NONE： 表示不缓存任何内容。
            DiskCacheStrategy.DATA： 表示只缓存原始图片。
            DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
            DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
            DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）
         */defaultOption!!.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
    }

    fun loadImage(iv: ImageView?, url: String?) {
        Glide.with(BaseApplication.instance()).load(url).apply(defaultOption!!).into(iv!!)
    }

    fun loadGif(iv: ImageView?, gif: String?) {
        Glide.with(BaseApplication.instance()).asGif().load(gif).apply(defaultOption!!).into(iv!!)
    }

    fun loadImage(iv: ImageView?, url: Int) {
        Glide.with(BaseApplication.instance()).load(url).apply(defaultOption!!).into(iv!!)
    }

    fun loadImageNoOption(iv: ImageView?, url: Int) {
        Glide.with(BaseApplication.instance()).load(url).into(iv!!)
    }

    fun loadImage(iv: ImageView?, url: Bitmap?) {
        Glide.with(BaseApplication.instance()).load(url).apply(defaultOption!!).into(iv!!)
    }

    fun loadImage(iv: ImageView, url: String?, placeholder: Int) {
        iv.tag = null
        val options: RequestOptions = RequestOptions()
            .format(DecodeFormat.PREFER_RGB_565)
            .error(placeholder)
            .placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        Glide.with(BaseApplication.instance()).load(url).apply(options).into(iv)
    }

    fun loadImage(iv: ImageView?, url: Int, placeholder: Int) {
        val options =
            RequestOptions().error(placeholder).placeholder(placeholder).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        Glide.with(BaseApplication.instance()).load(url).apply(options).into(iv!!)
    }

    fun loadImage(iv: ImageView, url: String?, placeholder: Int, mRadius: Int) {
        val options = RequestOptions.noAnimation().transform(CropRoundRadiusTransformation(iv.context, mRadius))
            .error(placeholder).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        Glide.with(BaseApplication.instance()).load(url).apply(options).into(iv)
    }

    /*
     *加载图片(默认)File，centerCrop，有占位图
     */
    fun loadImageFile(imageView: ImageView, file: File?) {
        imageView.tag = null
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .format(DecodeFormat.PREFER_RGB_565)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(BaseApplication.instance()).asBitmap().load(file).apply(options).into(imageView)
    }

    fun loadImagePreview(imageView: PhotoView, url: String?) {
        imageView.tag = null
        val options: RequestOptions = RequestOptions()
            .format(DecodeFormat.PREFER_RGB_565)
            .error(R.mipmap.ic_placeholder_2)
            .placeholder(R.mipmap.ic_placeholder_2)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        Glide.with(BaseApplication.instance()).load(url).dontTransform().override(-1).apply(options).into(imageView)
    }

    fun loadLongImage(iv: ImageView, bitmap: Bitmap?) {
        iv.tag = null
        val options: RequestOptions = RequestOptions()
            .format(DecodeFormat.PREFER_RGB_565)
            .error(R.mipmap.ic_placeholder)
            .placeholder(R.mipmap.ic_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(BaseApplication.instance()).load(bitmap).apply(options).into(iv)
    }

    companion object {
        private var mGlideUtils: GlideUtils? = null

        /**
         * 返回默认的配置
         *
         * @return
         */
        var defaultOption: RequestOptions? = null
        val instance: GlideUtils?
            get() {
                if (mGlideUtils == null) {
                    synchronized(GlideUtils::class.java) {
                        if (mGlideUtils == null) {
                            mGlideUtils = GlideUtils()
                        }
                    }
                }
                return mGlideUtils
            }
    }

    init {
        initOption()
    }
}