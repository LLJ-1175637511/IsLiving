package com.llj.living.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import androidx.annotation.Nullable


class CircleDrawable(mBitmap: Bitmap) : Drawable() {
    private val mPaint: Paint
    private val mWidth : Int//宽/高，直径

    override fun draw(@NonNull canvas: Canvas) {
        canvas.drawCircle(
            (mWidth / 2).toFloat(),
            (mWidth / 2).toFloat(),
            (mWidth / 2).toFloat(), mPaint
        ) //绘制圆形
    }

    override fun setAlpha( i: Int) {
        mPaint.alpha = i
    }

    override fun setColorFilter(@Nullable colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT //设置系统默认，让drawable支持和窗口一样的透明度
    }

    //还需要从重写以下2个方法，返回drawable实际宽高
    override fun getIntrinsicWidth(): Int {
        return mWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mWidth
    }

    init {
        val bitmapShader =
            BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP) //着色器 水平和竖直都需要填充满
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.shader = bitmapShader
        mWidth = Math.min(mBitmap.width, mBitmap.height) //宽高的最小值作为直径
    }
}