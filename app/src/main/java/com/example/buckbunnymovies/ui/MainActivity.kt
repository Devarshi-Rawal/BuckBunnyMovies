package com.example.buckbunnymovies.ui

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.buckbunnymovies.BuildConfig
import com.example.buckbunnymovies.databinding.ActivityMainBinding
import com.example.buckbunnymovies.db.entities.VideoEntity
import com.example.buckbunnymovies.ui.viewmodels.VideosViewModelFactory
import com.example.buckbunnymovies.ui.viewmodels.VideosViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), VideoListAdapter.OnDownloadClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: VideosViewModel by viewModels { VideosViewModelFactory(this.application) }
    var videoEntity = VideoEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.readMoviesFile(this)
        populateData()
    }

    private fun populateData() {
        lifecycleScope.launch {
            viewModel.getAllVideos.collect {
                binding.recyclerViewMovies.adapter =
                    VideoListAdapter(it, this@MainActivity, this@MainActivity)
            }
        }
    }

    override fun onDownloadClickListener(video: VideoEntity) {
        viewModel.updateIsDownloaded(true, video.videoId)
        if (!video.isDownloaded) {
            videoEntity = video
            try {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                } else {
                   downloadFile(video)
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG)
                    Log.e("DownloadManagerError", "onDownloadClickListener: ${e.stackTrace}")
            }
        } else {
            /**Current sending direct online video URL to exoplayer activity.
            * We need to maintain a list of filenames of downloaded file, access it based on video id
            * and send the file url to videoplayer activity
            * */

            val intent = Intent(this,VideoPlayerActivity::class.java)
            intent.putExtra("videoUrl",video.sources)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(videoEntity)
                } else{
                    Toast.makeText(this, "Please grant storage permission!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun downloadFile(video: VideoEntity){
        val request = DownloadManager.Request(Uri.parse(video.sources))
            .setTitle(video.title)
            .setDescription("${video.title} Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,"${video.videoId}_${video.title}")

        //Populate the list with the filename once video is downloaded

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

}