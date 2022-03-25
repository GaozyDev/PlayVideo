package com.gzy.playvideo.ui.detail

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.gzy.playvideo.R
import com.gzy.playvideo.video.VideoManager
import com.gzy.playvideo.video.data.SDKConstant
import com.gzy.playvideo.video.view.VideoView

class VideoDetailActivity : AppCompatActivity() {

    private lateinit var mFlVideoParent: FrameLayout

    private var mVideoManager: VideoManager = VideoManager.getInstance()

    private var mVideoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_video_detail)
        val shareVideoView = intent.getBooleanExtra(SHARE_VIDEO_VIEW, false)

        initView()
        if (shareVideoView) {
            initVideoView()
        }
    }

    private fun initView() {
        mFlVideoParent = findViewById(R.id.fl_video_detail_parent)
    }

    private fun initVideoView() {
        val width = this.windowManager.defaultDisplay.width
        val height = (width * SDKConstant.VIDEO_HEIGHT_PERCENT).toInt()
        val layoutParams = FrameLayout.LayoutParams(width, height)
        mVideoView = mVideoManager.videoView
        mVideoView?.let {
            it.layoutParams = layoutParams
            it.setVideoInitListener { it.start() }
            it.setVideoPlayerListener(null)
            mFlVideoParent.addView(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK)
        mVideoView?.let {
            it.pause()
            VideoManager.getInstance().videoView = it
            val parent = it.parent as ViewGroup
            parent.removeView(mVideoView)
        }
    }

    companion object {
        const val SHARE_VIDEO_VIEW = "share_video_view"
    }
}