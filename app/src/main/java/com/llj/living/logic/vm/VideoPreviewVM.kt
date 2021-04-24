package com.llj.living.logic.vm

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.custom.view.MyVideoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoPreviewVM(application: Application, savedStateHandle: SavedStateHandle) : BaseViewModel(
    application, savedStateHandle
) {

    private val mediaPlayer = MyVideoPlayer()
    private val _videoSizeLiveData = MutableLiveData(Pair(0, 0))
    val videoSizeLiveData: LiveData<Pair<Int, Int>> = _videoSizeLiveData
    private val _isPrepared = MutableLiveData(View.VISIBLE)
    val isPrepared: LiveData<Int> = _isPrepared
    private val _currentPlayingTime = MutableLiveData(0)
    val currentPlayingTime: LiveData<Int> = _currentPlayingTime
    private val _allPlayingTime = MutableLiveData(0)
    val allPlayingTime: LiveData<Int> = _allPlayingTime

    fun getPlayer() = mediaPlayer

    fun changeCurrentProgress(progress: Int) {
        val newProcess = progress * 1000 + mediaPlayer.currentPosition % 1000
        mediaPlayer.seekTo(newProcess)
        _currentPlayingTime.postValue(newProcess)
    }

    fun loadVideo(uri: Uri) {
        mediaPlayer.apply {
            setDataSource(getApplication(), uri)
            setOnVideoSizeChangedListener { _, width, height ->
                _videoSizeLiveData.value = Pair(width, height)
            }
            setOnPreparedListener {
                isLooping = false
                _isPrepared.postValue(View.INVISIBLE)
                _allPlayingTime.value = it.duration
                mediaPlayer.start()
                refreshCurrentTime()
            }
            prepareAsync()
        }
    }

    private fun refreshCurrentTime() {
        viewModelScope.launch {
            while (true) {
                delay(500)
                if (mediaPlayer.isPlaying) {
                    _currentPlayingTime.postValue(mediaPlayer.currentPosition)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}