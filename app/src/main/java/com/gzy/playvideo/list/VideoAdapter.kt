package com.gzy.playvideo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gzy.playvideo.R
import com.gzy.playvideo.video.VideoContext
import com.gzy.playvideo.video.VideoData

class VideoAdapter(private val dataSet: List<VideoData>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private var mVideoContext: VideoContext? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_video, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position % 5 == 0) {
            mVideoContext = VideoContext(
                viewHolder.layout,
                dataSet[position]
            )
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateVideo() {
        mVideoContext?.updateVideo()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: FrameLayout = view as FrameLayout
    }
}
