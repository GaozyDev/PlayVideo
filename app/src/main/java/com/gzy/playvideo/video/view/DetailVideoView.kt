package com.gzy.playvideo.video.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.gzy.playvideo.R
import com.gzy.playvideo.video.data.SDKConstant

@SuppressLint("ViewConstructor")
class DetailVideoView(context: Context, val videoView: VideoView) : FrameLayout(context) {

    private lateinit var mClVideoParent: FrameLayout

    init {
        initView()
    }

    private fun initView() {
        val metrics: DisplayMetrics = this.resources.displayMetrics
        val width = metrics.widthPixels
        val height = (width * SDKConstant.VIDEO_HEIGHT_PERCENT).toInt()
        layoutParams = LayoutParams(width, height)
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_detail_video, this)
        mClVideoParent = findViewById(R.id.fl_detail_video_parent)

        initVideoView()
    }

    private fun initVideoView() {
        videoView.layoutParams = layoutParams
        videoView.setVideoPlayerListener(object : VideoView.VideoPlayListener {
            override fun onVideoPlayStart() {
            }

            override fun onVideoPlayFailed() {
            }

            override fun onVideoPlayComplete() {
            }

            override fun onVideoClick() {
            }
        })
        videoView.setVideoInitListener { videoView.start() }
        mClVideoParent.addView(videoView)
    }

    fun pause() {
        videoView.pause()
    }
}