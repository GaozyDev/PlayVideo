package com.gzy.playvideo.list

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gzy.playvideo.R
import com.gzy.playvideo.video.data.VideoData


class ListVideoActivity : AppCompatActivity(), View.OnClickListener, SurfaceHolder.Callback {

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

        val adapter = VideoAdapter(data)
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

}