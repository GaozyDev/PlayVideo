package com.gzy.playvideo.ui.detail

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.gzy.playvideo.R
import com.gzy.playvideo.video.VideoManager
import com.gzy.playvideo.video.view.DetailVideoView

class VideoDetailActivity : AppCompatActivity() {

    private lateinit var mFlVideoParent: FrameLayout

    private var mVideoManager: VideoManager = VideoManager.getInstance()

    private var mDetailVideoView: DetailVideoView? = null

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
        mDetailVideoView = mVideoManager.build().setParentView(mFlVideoParent)
            .createDetailVideoView(mVideoManager.videoView, object : DetailVideoView.DetailVideoListener {
                override fun onBackClick() {
                    onBackPressed()
                }

                override fun onFloatClick() {
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK)
        mDetailVideoView?.let {
            it.hideOrShowUI(true)
            it.videoView.pause()
            VideoManager.getInstance().videoView = it.videoView
            val parent = it.videoView.parent as ViewGroup
            parent.removeView(it.videoView)
        }
    }

    companion object {
        const val SHARE_VIDEO_VIEW = "share_video_view"
    }
}