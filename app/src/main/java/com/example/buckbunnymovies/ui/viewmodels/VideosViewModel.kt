package com.example.buckbunnymovies.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buckbunnymovies.BuildConfig
import com.example.buckbunnymovies.db.VideosDatabase
import com.example.buckbunnymovies.db.entities.VideoEntity
import com.example.buckbunnymovies.ui.models.Video
import com.example.buckbunnymovies.ui.repositories.VideosRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.InputStream


class VideosViewModel(val application: Application) : ViewModel() {

    private val repository: VideosRepository
    val getAllVideos: Flow<List<VideoEntity>>

    init {
        val dao = VideosDatabase.getInstance(application.applicationContext).videoDao()
        repository = VideosRepository(dao)
        getAllVideos = repository.getAllVideos()
    }

    private fun insert(videoId: Int,videoUrl: String, thumb: String, subtitle: String, description: String, title: String, isDownloaded: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(videoId,videoUrl,thumb,subtitle,description,title,isDownloaded)
        }
    }

    fun updateIsDownloaded(isDownloaded: Boolean,videoId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateIsDownloaded(isDownloaded,videoId)
        }
    }

    fun readMoviesFile(mContext: Context){
        val data: String
        val videoList: List<Video>
        try {
            val inputStream: InputStream = mContext.assets.open("movies.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            data = String(buffer, charset("UTF-8"))
            val jsonObject = JSONObject(data)
            val videosJsonArray = jsonObject.getJSONArray("categories").getJSONObject(0).getJSONArray("videos")

            val listCountryType = object : TypeToken<List<Video>>() {}.type
            videoList = Gson().fromJson(videosJsonArray.toString(),listCountryType)

            for (i in videoList.indices){
                insert(i+1,videoList[i].sources[0],videoList[i].thumb,videoList[i].subtitle,videoList[i].description,videoList[i].title,false)
            }

        } catch (ex: Exception) {
            if (BuildConfig.DEBUG)
                Log.e("MoviesDataReadException", "readMoviesFile: ${ex.stackTrace}")
        }
    }
}

class VideosViewModelFactory(private val mApplication: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideosViewModel(mApplication) as T
    }
}