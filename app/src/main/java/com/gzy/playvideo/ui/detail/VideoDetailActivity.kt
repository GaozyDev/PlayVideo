package com.gzy.playvideo.ui.detail

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.gzy.playvideo.R
import com.gzy.playvideo.video.VideoManager
import com.gzy.playvideo.video.data.SDKConstant
import com.gzy.playvideo.video.utils.DisplayUtil

class VideoDetailActivity : AppCompatActivity() {

    private lateinit var mFlVideoParent: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        initView()
    }

    private fun initView() {
        mFlVideoParent = findViewById(R.id.fl_video_detail_parent)
        mFlVideoParent.addView(VideoManager.getInstance().videoView)

        val dm = DisplayMetrics()
        (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels - DisplayUtil.dp2px(this, 20f)
        val height = (width * SDKConstant.VIDEO_HEIGHT_PERCENT).toInt()
        val layoutParams = FrameLayout.LayoutParams(width, height)
        VideoManager.getInstance().videoView.layoutParams = layoutParams
        VideoManager.getInstance().videoView.setVideoInitListener { VideoManager.getInstance().videoView.start() }
    }
}