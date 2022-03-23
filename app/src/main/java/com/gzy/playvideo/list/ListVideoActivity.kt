package com.gzy.playvideo.list

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gzy.playvideo.R
import com.gzy.playvideo.video.VideoContextInterface
import com.gzy.playvideo.video.VideoContext
import com.gzy.playvideo.video.VideoData


class ListVideoActivity : AppCompatActivity(), View.OnClickListener, SurfaceHolder.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_video)

        initView()
    }

    private fun initView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_list_video)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = VideoAdapter(
            listOf(
                VideoData(
                    "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
                    "https://img-baofun.zhhainiao.com/fs/42dc11435af1ee0339a17642a2d232d6.jpg"
                ),
                VideoData(
                    "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
                    "https://img-baofun.zhhainiao.com/fs/42dc11435af1ee0339a17642a2d232d6.jpg"
                ), VideoData(
                    "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
                    "https://img-baofun.zhhainiao.com/fs/42dc11435af1ee0339a17642a2d232d6.jpg"
                ), VideoData(
                    "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
                    "https://img-baofun.zhhainiao.com/fs/42dc11435af1ee0339a17642a2d232d6.jpg"
                ), VideoData(
                    "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
                    "https://img-baofun.zhhainiao.com/fs/42dc11435af1ee0339a17642a2d232d6.jpg"
                ), VideoData(
                    "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
                    "https://img-baofun.zhhainiao.com/fs/42dc11435af1ee0339a17642a2d232d6.jpg"
                )
            )
        )
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                adapter.updateVideo()
            }
        })
    }

    override fun onClick(v: View?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}