package com.gzy.playvideo.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gzy.playvideo.R
import com.gzy.playvideo.ui.detail.VideoDetailActivity
import com.gzy.playvideo.ui.detail.VideoDetailActivity.Companion.SHARE_VIDEO_VIEW
import com.gzy.playvideo.video.VideoManager
import com.gzy.playvideo.video.data.VideoData
import com.gzy.playvideo.video.view.VideoView


class VideoListActivity : AppCompatActivity(), View.OnClickListener, SurfaceHolder.Callback {

    private var mVideoViewParent: ViewGroup? = null;

    private lateinit var mAdapter: VideoAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_video)

        initView()
    }

    private fun initView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_list_video)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val data = mutableListOf<VideoData>()

        val urls = listOf(
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4",
            "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4",
            "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319104618910544.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4",
            "http://vfx.mtime.cn/Video/2019/03/17/mp4/190317150237409904.mp4",
            "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4",
            "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4",
            "http://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4",
            "http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4",
            "http://vfx.mtime.cn/Video/2019/03/12/mp4/190312083533415853.mp4",
            "http://vfx.mtime.cn/Video/2019/03/09/mp4/190309153658147087.mp4"
        )

        for (url in urls) {
            data.add(
                VideoData(
                    url,
                    "https://images7.alphacoders.com/550/thumb-1920-550739.jpg"
                )
            )
        }

        mAdapter = VideoAdapter(data)
        recyclerView.adapter = mAdapter
        mAdapter.mAdapterListener = object : VideoAdapter.AdapterListener {
            override fun onVideoClick(videoView: VideoView) {
                videoView.pause()
                videoView.setIsProcessDetached(false)
                VideoManager.getInstance().videoView = videoView
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@VideoListActivity,
                    videoView.parent as View,
                    getString(R.string.share_video_view)
                ).toBundle()
                mVideoViewParent = videoView.parent as ViewGroup
                mVideoViewParent?.removeView(videoView)
                val intent = Intent(this@VideoListActivity, VideoDetailActivity::class.java)
                intent.putExtra(SHARE_VIDEO_VIEW, true)
                startActivityForResult(intent, VIDEO_DETAIL_CODE, bundle)
            }
        }
    }

    override fun onClick(v: View?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == VIDEO_DETAIL_CODE) {
            val videoView = VideoManager.getInstance().videoView
            mAdapter.backToAdapter(videoView)
        }
    }

    companion object {
        const val VIDEO_DETAIL_CODE = 1
    }
}