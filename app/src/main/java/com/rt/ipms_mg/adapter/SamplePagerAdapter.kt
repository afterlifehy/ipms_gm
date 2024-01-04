package com.rt.ipms_mg.adapter

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.ImageUtils
import com.github.chrisbanes.photoview.PhotoView
import com.rt.base.BaseApplication
import com.rt.base.ext.i18n
import com.rt.base.help.ActivityCacheManager
import com.rt.base.util.ToastUtil
import com.rt.common.util.GlideUtils
import com.rt.ipms_mg.R
import java.util.ArrayList

class SamplePagerAdapter(
    var onClickListener: View.OnClickListener?,
    var list: ArrayList<String>?
) : PagerAdapter() {
    var saveBuilder: AlertDialog.Builder? = null
    var bitmap: Bitmap? = null
    var drawable: Drawable? = null
    private val cacheView: View? = null

    private val onDismissListener = DialogInterface.OnDismissListener {
        if (null != cacheView) {
            cacheView.isEnabled = true
        }
    }
    var onLongClickListener: View.OnLongClickListener? = View.OnLongClickListener { v ->
        drawable = (v as PhotoView).drawable
        if (drawable is BitmapDrawable) {
            bitmap = (drawable as BitmapDrawable).bitmap
            saveBuilder!!.show()
        }
        true
    }

    init {
        saveBuilder =
            ActivityCacheManager.instance().getCurrentActivity()?.let { AlertDialog.Builder(it) }
        saveBuilder!!.setMessage(i18n(com.rt.base.R.string.保存图片))
        saveBuilder!!.setPositiveButton(i18n(com.rt.base.R.string.确定)) { dialog, _ ->
            dialog.dismiss()
            ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG)
            i18n(com.rt.base.R.string.保存成功).let { ToastUtil.showMiddleToast(it) }
        }
        saveBuilder!!.setOnDismissListener(onDismissListener)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val convertView =
            View.inflate(BaseApplication.instance(), R.layout.item_preview_img, null)
        if (list != null) {
            var url = ""
            url = list!![position]
            GlideUtils.instance?.loadImagePreview(convertView.findViewById(R.id.pv_img), url)
            (convertView.findViewById(R.id.pv_img) as PhotoView).maximumScale = 10.0f
        }
        container.addView(
            convertView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        convertView.findViewById<PhotoView>(R.id.pv_img)
            .setOnLongClickListener(onLongClickListener)
        convertView.findViewById<PhotoView>(R.id.pv_img).setOnClickListener(onClickListener)
        return convertView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val contentView = `object` as View
        container.removeView(contentView)
        if (`object` is PhotoView) {
            val s = `object`
            val bitmapDrawable = s.drawable as BitmapDrawable
            val bm = bitmapDrawable.bitmap
            if (bm != null && !bm.isRecycled) {
                s.setImageResource(0)
                bm.recycle()
            }
        }
    }

    override fun getCount(): Int {
        return if (list == null) 1 else list!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}