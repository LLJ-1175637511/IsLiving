package com.llj.living.custom.view

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyChronometer : Chronometer, LifecycleObserver {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var elapseTime = 0L

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseAR() {
        elapseTime = SystemClock.elapsedRealtime() - base
        stop()
    }

    fun resumeAR() {
        base = SystemClock.elapsedRealtime() - elapseTime
        start()
    }


    fun startAR() {
        base = SystemClock.elapsedRealtime()
        start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopAR() {
        base = SystemClock.elapsedRealtime()
        elapseTime = 0
        stop()
    }
}