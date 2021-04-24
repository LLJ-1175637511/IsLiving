package com.llj.living.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.llj.living.R
import com.llj.living.custom.ext.toastShort
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity() {
    @SuppressLint("HardwareIds", "MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val str = """<html>
<body>

<h1>My First Heading</h1>

<p>My first paragraph.</p>

</body>
</html>"""
        webView.loadData(str, "text/html", null)

        val mTelephonyMgr =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mTelephonyMgr.imei
        }else mTelephonyMgr.deviceId
         //获取IMEI号
        toastShort(imei)
    }
}