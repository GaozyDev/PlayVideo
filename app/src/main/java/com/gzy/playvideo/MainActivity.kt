package com.gzy.playvideo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.gzy.playvideo.ui.list.VideoListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initClick()
    }

    private fun initClick() {
        findViewById<Button>(R.id.btn_main_list_video).setOnClickListener {
            startActivity(Intent(this, VideoListActivity::class.java))
        }
    }
}