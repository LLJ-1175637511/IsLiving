package com.llj.living.ui.activity

import android.Manifest
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity2 : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var locationManager: LocationManager
    private var locationProvider: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }//不为空,显示地理位置经纬度//监视地理位置变化//输入经纬度//监视地理位置变化//请求权限//获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）//如果是Network

    override fun onStart() {
        super.onStart()
        getLocation()
    }

    //获取所有可用的位置提供器
    private fun getLocation() {
        //获取所有可用的位置提供器
        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
            isSpeedRequired = false
            isAltitudeRequired = false
            isBearingRequired = false
            powerRequirement = Criteria.POWER_MEDIUM
        }
        val providers = locationManager.getBestProvider(criteria, true)
        when (providers) {
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
            null -> {
                val i = Intent().apply {
                    action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                }
                startActivity(i)
            }
            else -> {
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //请求权限
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE
                    ), LOCATION_CODE
                )
            } else {
                //监视地理位置变化
                locationManager.requestLocationUpdates(
                    locationProvider!!,
                    3000,
                    1f,
                    locationListener
                )
                val location: Location? = locationManager.getLastKnownLocation(
                    locationProvider!!
                )
                if (location != null) {
                    //输入经纬度
                    Toast.makeText(
                        this,
                        location.getLongitude().toString() + " " + location.getLatitude() + "",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            //监视地理位置变化
            locationManager.requestLocationUpdates(
                locationProvider!!,
                3000,
                1f,
                locationListener
            )
            val location: Location? = locationManager.getLastKnownLocation(locationProvider!!)
            if (location != null) {
                //不为空,显示地理位置经纬度
                Toast.makeText(
                    this,
                    location.getLongitude().toString() + " " + location.getLatitude() + "",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private var locationListener: LocationListener = object : LocationListener {
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        override fun onLocationChanged(location: Location) {
            val loaX = location.longitude
            val loaY = location.latitude
            lifecycleScope.launch(Dispatchers.IO) {
                val bean = SystemRepository.getBaiduLLRequest(loaX, loaY)
                val baiduX = Base64.decode(bean.x, Base64.DEFAULT)
                val baiduY = Base64.decode(bean.y, Base64.DEFAULT)
                val dX = String(baiduX).toDouble()
                val dY = String(baiduY).toDouble()
                LogUtils.d("Test2", "gps:$loaX $loaY")
                LogUtils.d("Test2", "baidu:${dX} ${dY}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@TestActivity2,
                        "$baiduX $baiduY",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {}

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "申请权限", Toast.LENGTH_LONG).show()
                try {
                    val providers =
                        locationManager.getProviders(true)
                    if (providers.contains(LocationManager.GPS_PROVIDER)) {
                        //如果是GPS
                        locationProvider = LocationManager.GPS_PROVIDER
                    } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                        //如果是Network
                        locationProvider = LocationManager.NETWORK_PROVIDER
                    }
                    //监视地理位置变化
                    locationManager.requestLocationUpdates(
                        locationProvider!!,
                        3000,
                        1f,
                        locationListener
                    )
                    val location: Location? = locationManager.getLastKnownLocation(
                        locationProvider!!
                    )
                    if (location != null) {
                        //不为空,显示地理位置经纬度
                        LogUtils.d(
                            "Test2",
                            location.getLongitude().toString() + " " + location.getLatitude()
                        )
                        Toast.makeText(
                            this,
                            location.getLongitude().toString() + " " + location.getLatitude() + "",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, "缺少权限", Toast.LENGTH_LONG).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    companion object {
        private const val LOCATION_CODE = 301
    }
}
