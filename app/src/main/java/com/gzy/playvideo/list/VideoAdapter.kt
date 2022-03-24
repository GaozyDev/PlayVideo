package com.gzy.playvideo.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gzy.playvideo.R
import com.gzy.playvideo.video.VideoManager
import com.gzy.playvideo.video.data.VideoData
import com.gzy.playvideo.video.view.ListVideoView

class VideoAdapter(private val dataSet: List<VideoData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mVideoManager: VideoManager = VideoManager.getInstance()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_video, viewGroup, false)

        return if (viewType == 1) {
            VideoHolder(view)
        } else {
            ImageHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is VideoHolder) {
            if (viewHolder.videoView == null) {
                viewHolder.videoView = mVideoManager.build(
                    viewHolder.layout
                ).createListVideoView()
            }

            viewHolder.videoView!!.loadPreview(dataSet[position].preview)
            viewHolder.videoView!!.setVideoInitListener {
                viewHolder.videoView!!.loadVideo(dataSet[position].url)
            }
        }
    }

    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        if (position % 4 == 0) {
            return 1
        }
        return super.getItemViewType(position)
    }

    fun updateVideo() {

    }

    class VideoHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: FrameLayout = view as FrameLayout
        var videoView: ListVideoView? = null
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view)
}
