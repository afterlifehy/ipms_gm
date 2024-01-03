package com.rt.common.view.keyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import com.blankj.utilcode.util.SizeUtils
import com.rt.common.R


class MyKeyboardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : KeyboardView(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val keyboard = keyboard ?: return
        val keys = keyboard.keys
        if (keys != null && keys.size > 0) {
            val paint = Paint()
            paint.setColor(getContext().getResources().getColor(com.rt.base.R.color.color_ff04a091));
            paint.setTextAlign(Paint.Align.CENTER)
            val font: Typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.setTypeface(font)
            paint.setAntiAlias(true)


            //此处进行特殊按键的背景色处理
            for (key in keys) {
                if (key.codes[0] == -2) {
                    paint.setColor(getContext().getResources().getColor(com.rt.base.R.color.color_ff04a091));
                    val rect = RectF(
                        key.x.toFloat(),
                        key.y.toFloat() + SizeUtils.dp2px(10f),
                        (
                                key.x + key.width).toFloat(),
                        key.y + key.height + SizeUtils.dp2px(10f).toFloat()
                    )
                    canvas.drawRoundRect(
                        rect,
                        SizeUtils.dp2px(4f).toFloat(),
                        SizeUtils.dp2px(4f).toFloat(),
                        paint
                    )

                    paint.setColor(getContext().getResources().getColor(R.color.white));
                    paint.textSize = SizeUtils.dp2px(21f).toFloat()
                    canvas.drawText(
                        "其他",
                        key.x.toFloat() + key.width / 2,
                        key.y.toFloat() + key.height / 2 + key.height / 4 + SizeUtils.dp2px(5f),
                        paint
                    )

                }

                if (key.codes[0] == -1) {
                    paint.setColor(getContext().getResources().getColor(com.rt.base.R.color.color_ff04a091));
                    val rect = RectF(
                        key.x.toFloat(),
                        key.y.toFloat() + SizeUtils.dp2px(10f),
                        (
                                key.x + key.width).toFloat(),
                        key.y + key.height + SizeUtils.dp2px(10f).toFloat()
                    )
                    canvas.drawRoundRect(
                        rect,
                        SizeUtils.dp2px(4f).toFloat(),
                        SizeUtils.dp2px(4f).toFloat(),
                        paint
                    )

                    paint.setColor(getContext().getResources().getColor(R.color.white));
                    paint.textSize = SizeUtils.dp2px(21f).toFloat()
                    canvas.drawText(
                        "返回",
                        key.x.toFloat() + key.width / 2,
                        key.y.toFloat() + key.height / 2 + key.height / 4 + SizeUtils.dp2px(5f),
                        paint
                    )
                }


                if (key.codes[0] == -3) {
                    val paint = Paint()
                    paint.setColor(getContext().getResources().getColor(com.rt.base.R.color.color_ff04a091));

                    val rect = RectF(
                        key.x.toFloat(),
                        key.y.toFloat() + SizeUtils.dp2px(10f),
                        (
                                key.x + key.width).toFloat(),
                        key.y + key.height + SizeUtils.dp2px(10f).toFloat()
                    )
                    canvas.drawRoundRect(
                        rect,
                        SizeUtils.dp2px(4f).toFloat(),
                        SizeUtils.dp2px(4f).toFloat(),
                        paint
                    )

                    val dr = context.resources.getDrawable(R.mipmap.ic_keyboard_delete) as Drawable
                    dr.setBounds(
                        key.x + key.width / 4,
                        key.y + key.height / 3 + SizeUtils.dp2px(9f),
                        key.x + key.width / 4 + key.width / 4 + key.width / 4,
                        key.y + key.height - SizeUtils.dp2px(6f)
                    )
                    dr.draw(canvas)
                }
                if (key.codes[0] == -4) {
                    paint.setColor(getContext().getResources().getColor(com.rt.base.R.color.white));
                    val rect = RectF(
                        key.x.toFloat(),
                        key.y.toFloat() + SizeUtils.dp2px(10f),
                        (
                                key.x + key.width).toFloat(),
                        key.y + key.height + SizeUtils.dp2px(10f).toFloat()
                    )
                    canvas.drawRoundRect(
                        rect,
                        SizeUtils.dp2px(4f).toFloat(),
                        SizeUtils.dp2px(4f).toFloat(),
                        paint
                    )
                    paint.setColor(getContext().getResources().getColor(com.rt.base.R.color.black));
                    paint.textSize = SizeUtils.dp2px(30f).toFloat()
                    canvas.drawText(
                        key.label.toString(),
                        key.x.toFloat() + key.width / 2,
                        key.y.toFloat() + key.height / 2 + key.height / 4 + SizeUtils.dp2px(5f),
                        paint
                    )
                }
            }
        }
    }

}