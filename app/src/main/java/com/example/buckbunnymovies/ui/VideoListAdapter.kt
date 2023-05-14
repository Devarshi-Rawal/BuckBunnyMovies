package com.example.buckbunnymovies.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buckbunnymovies.R
import com.example.buckbunnymovies.databinding.ItemMovieBinding
import com.example.buckbunnymovies.db.entities.VideoEntity

class VideoListAdapter(val videoList: List<VideoEntity>,val onDownloadClickListener: OnDownloadClickListener,val mContext: Context) : RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder>() {

    //val videoList = mutableListOf<VideoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoListViewHolder(binding)
    }

    override fun getItemCount(): Int = videoList.size

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.bind(videoList[position],onDownloadClickListener,mContext)
    }

    class VideoListViewHolder(val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(video: VideoEntity,onDownloadClickListener: OnDownloadClickListener, mContext: Context){
            binding.video = video
            binding.materialButtonDownload.setOnClickListener {
                //binding.materialButtonDownload.text = mContext.resources.getString(R.string.play)
                binding.materialButtonDownload.text = mContext.resources.getString(R.string.play)
                onDownloadClickListener.onDownloadClickListener(video)
            }
        }
    }

    /*fun setData(list: List<VideoEntity>){
        videoList.addAll(list)
        notifyItemRangeChanged(0,list.size)
    }*/

    interface OnDownloadClickListener{
        fun onDownloadClickListener(video: VideoEntity)
    }
}