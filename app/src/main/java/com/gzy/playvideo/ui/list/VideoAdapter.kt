package com.gzy.playvideo.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gzy.playvideo.R
import com.gzy.playvideo.video.VideoManager
import com.gzy.playvideo.video.data.VideoData
import com.gzy.playvideo.video.view.ListVideoView
import com.gzy.playvideo.video.view.VideoView

class VideoAdapter(private val dataSet: List<VideoData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mVideoManager: VideoManager = VideoManager.getInstance()

    var mAdapterListener: AdapterListener? = null

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

                viewHolder.videoView?.setListVideoListener {
                    mAdapterListener?.onVideoClick(viewHolder.videoView!!, it)
                }
            }

            viewHolder.videoView!!.loadPreview(dataSet.random().preview)
            viewHolder.videoView!!.setVideoInitListener {
                viewHolder.videoView!!.loadVideo(dataSet.random().url)
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

    interface AdapterListener {
        fun onVideoClick(parentView: ListVideoView, videoView: VideoView)
    }
}
