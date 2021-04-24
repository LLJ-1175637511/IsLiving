package com.llj.living.custom.view

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyVideoPlayer:MediaPlayer(),LifecycleObserver {

    private var isCreated = false

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pausePlayer(){
        pause()
    }

    fun resumePlayer(){
        if (isCreated) start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        isCreated = true
    }

}