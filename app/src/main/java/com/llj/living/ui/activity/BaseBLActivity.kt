package com.llj.living.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.toastShort
import com.llj.living.custom.ext.tryException
import com.llj.living.custom.ext.tryExceptionLaunch
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers

/**
 * BaseBL:BaseBaiduLocation
 */
abstract class BaseBLActivity<DB : ViewDataBinding> : BaseActivity<DB>() {

    private lateinit var locationManager: LocationManager
    private var locationProvider: String? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !allPermissionsGranted()) {
            //请求权限
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, LOCATION_CODE)
        } else {
            getBaiduLocation()
        }
    }

    private fun getBaiduLocation() {
        val network =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!(gps || network)) {//申请打开定位
            val i = Intent().apply {
                action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            }
            startActivity(i)
            return
        }
        //获取所有可用的位置提供器
        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
            isSpeedRequired = false
            isAltitudeRequired = false
            isBearingRequired = false
            powerRequirement = Criteria.POWER_MEDIUM
        }

        when (locationManager.getBestProvider(criteria, true)) {
            LocationManager.NETWORK_PROVIDER -> {
                LogUtils.d("Test2", "NETWORK_PROVIDER")
                locationProvider = LocationManager.NETWORK_PROVIDER
            }
            LocationManager.GPS_PROVIDER -> {
                LogUtils.d("Test2", "GPS_PROVIDER")
                locationProvider = LocationManager.GPS_PROVIDER
            }
            LocationManager.PASSIVE_PROVIDER -> {
                LogUtils.d("Test2", "PASSIVE_PROVIDER")
                locationProvider = LocationManager.PASSIVE_PROVIDER
            }
            else -> {
            }
        }
        startGetLocation()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_CODE && grantResults.isNotEmpty()) {
            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) {
                    toastShort("请先允许系统使用权限")
                    finish()
                    return
                }
            }
            getBaiduLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startGetLocation() = tryException("获取定位信息") {
        locationProvider?.let { lp ->
            locationManager.requestLocationUpdates(
                lp,
                30000,
                1f,
                locationListener
            )
            val location = locationManager.getLastKnownLocation(lp)
            location?.let {
                convertLoc(it.longitude,it.latitude)
            }
        }
    }

    private var locationListener: LocationListener = object : LocationListener {
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        override fun onLocationChanged(location: Location) {
            val lon = location.longitude
            val lat = location.latitude
            convertLoc(lon,lat)
        }

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {}

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {}
    }

    private fun convertLoc(lon: Double, lat: Double) {
        tryExceptionLaunch(Dispatchers.IO,"转换地理坐标") {
            val bean = SystemRepository.getBaiduLLRequest(lon, lat)
            val baiduLon = Base64.decode(bean.x, Base64.DEFAULT)
            val baiduLat = Base64.decode(bean.y, Base64.DEFAULT)
            val dLon = String(baiduLon).toDouble()
            val dLat = String(baiduLat).toDouble()
            MyApplication.setLocation(Pair(dLon, dLat))
            LogUtils.d(TAG, "坐标 lng:$dLon lat:$dLat")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    companion object {
        private const val LOCATION_CODE = 301
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
    }
}