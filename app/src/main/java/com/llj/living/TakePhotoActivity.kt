package com.llj.living

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_take_photo.*
import java.io.File
import java.io.IOException

class TakePhotoActivity : AppCompatActivity() {
    companion object {
        //控制两种打开方式
        const val TAKE_PHOTO = 1
        const val CHOOSE_PHOTO = 2
    }

    private var bitmap:Bitmap ?= null

    private val mLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                // 将拍摄的照片显示出来
                bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                img_view.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        take_photo.setOnClickListener {
            val outputImage = File(externalCacheDir, "output_image.jpg")
            try {
                if (outputImage.exists()) {
                    outputImage.delete()
                }
                outputImage.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            imageUri = if (Build.VERSION.SDK_INT < 24) {
                Uri.fromFile(outputImage)
            } else {
                FileProvider.getUriForFile(
                    this,
                    "com.llj.living.fileprovider",//定义唯一标识，关联后面的内容提供器
                    outputImage
                )
            }

            /*// 3. 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, TAKE_PHOTO)*/

            mLauncher.launch(
                Intent("android.media.action.IMAGE_CAPTURE").putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    imageUri
                )
            )

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == RESULT_OK) {
                try {
                    // 将拍摄的照片显示出来
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                    img_view.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
*/
        }
    }
}