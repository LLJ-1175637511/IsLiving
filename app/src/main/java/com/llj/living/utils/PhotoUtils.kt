package com.llj.living.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException


object PhotoUtils {
    fun getBitmapFromWH(
        url: String,
        width: Double,
        height: Double
    ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true // 设置了此属性一定要记得将值设置为false
        val bitmap = BitmapFactory.decodeFile(url)
        // 防止OOM发生
        options.inJustDecodeBounds = false
        val mWidth = bitmap.width
        val mHeight = bitmap.height

        val matrix = Matrix()
        var scaleWidth = 1f
        var scaleHeight = 1f
        // 按照固定宽高进行缩放
        if (mWidth <= mHeight) {
            scaleWidth = (width / mWidth).toFloat()
            scaleHeight = (height / mHeight).toFloat()
        } else {
            scaleWidth = (height / mWidth).toFloat()
            scaleHeight = (width / mHeight).toFloat()
        }
        // 按照固定大小对图片进行缩放
        matrix.postScale(scaleWidth, scaleHeight)
        val newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true)
        // 用完了记得回收
        bitmap.recycle()
        return newBitmap
    }

    fun getBitmapFromScale(
        bitmap: Bitmap,
        scaleValue: Int
    ): Bitmap? {
        val byteCount = bitmap.byteCount / 1024
        LogUtils.d("FaceAuthenticActivity", "byteCount $byteCount")
        //压缩倍数
        val scale = if (byteCount > scaleValue) (scaleValue / byteCount).toFloat()
        else 1.0f
        var scaleWidth = 1f
        var scaleHeight = 1f
        val mWidth = bitmap.width
        val mHeight = bitmap.height
        if (mWidth <= mHeight) {
            scaleWidth = (mWidth / 2).toFloat()
            scaleHeight = (mHeight / 2).toFloat()
        } else {
            scaleWidth = (mWidth / 2).toFloat()
            scaleHeight = (mHeight / 2).toFloat()
        }
        val matrix = Matrix()
        // 按照固定宽高进行缩放
        matrix.postScale(scaleWidth, scaleHeight)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true // 设置了此属性一定要记得将值设置为false
        val new = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true)
        // 防止OOM发生
        options.inJustDecodeBounds = false
        bitmap.recycle()
        return new
    }

    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes: ByteArray = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

}