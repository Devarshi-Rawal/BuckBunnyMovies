package com.example.buckbunnymovies.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.buckbunnymovies.databinding.ActivityVideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class VideoPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoPlayerBinding
    lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)
        setExoPlayer()
    }

    private fun setExoPlayer(){
        exoPlayer = ExoPlayer.Builder(this).build()
        binding.exoPlayerViewVideo.player = exoPlayer
        val mediaItem: MediaItem = MediaItem.fromUri(Uri.parse(intent.getStringExtra("videoUrl").toString()))
        exoPlayer.addMediaItem(mediaItem)
        val currentVolume = exoPlayer.deviceVolume
        exoPlayer.volume = currentVolume.toFloat()
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.stop()
    }
}