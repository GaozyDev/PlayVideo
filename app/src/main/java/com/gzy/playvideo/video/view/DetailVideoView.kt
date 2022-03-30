package com.gzy.playvideo.video.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.gzy.playvideo.R
import com.gzy.playvideo.video.data.SDKConstant


@SuppressLint("ViewConstructor")
class DetailVideoView(
    context: Context,
    val videoView: VideoView,
    private val detailVideoListener: DetailVideoListener
) : FrameLayout(context) {

    private lateinit var mFlVideoParent: FrameLayout

    private lateinit var mIvGreyTop: ImageView
    private lateinit var mIvBack: ImageView
    private lateinit var mIvFloat: ImageView
    private lateinit var mFlLoading: FrameLayout
    private lateinit var mTvLoading: TextView
    private lateinit var mTvProgress: TextView
    private lateinit var mIvGreyBottom: ImageView
    private lateinit var mIvPlay: ImageView
    private lateinit var mSeekBar: SeekBar
    private lateinit var mTvDuration: TextView
    private lateinit var mIvFull: ImageView

    private lateinit var mTopUI: List<View>
    private lateinit var mBottomUI: List<View>

    private var mIsPlaying = false

    private val mUiCountDown: CountDownTimer

    private var mIsUIShow = true

    private var mLastAnimStartTime: Long = 0

    private var mUserTouch = false;

    init {
        initView()
        mUiCountDown = object : CountDownTimer(1000 * 5L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (mIsUIShow) {
                    hideOrShowUI()
                    mIsUIShow = false
                }
            }
        }
        mUiCountDown.start()
    }

    private fun initView() {
        val metrics: DisplayMetrics = this.resources.displayMetrics
        val width = metrics.widthPixels
        val height = (width * SDKConstant.VIDEO_HEIGHT_PERCENT).toInt()
        layoutParams = LayoutParams(width, height)
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_detail_video, this)
        mFlVideoParent = findViewById(R.id.fl_detail_video_parent)
        mIvGreyTop = findViewById(R.id.iv_detail_video_grey_top)
        mIvBack = findViewById(R.id.iv_detail_video_back)
        mIvFloat = findViewById(R.id.iv_detail_video_float)
        mFlLoading = findViewById(R.id.fl_detail_video_loading)
        mTvLoading = findViewById(R.id.tv_detail_video_loading)
        mTvProgress = findViewById(R.id.tv_detail_video_progress)
        mIvGreyBottom = findViewById(R.id.iv_detail_video_grey_bottom)
        mIvPlay = findViewById(R.id.iv_detail_video_play)
        mSeekBar = findViewById(R.id.sb_detail_video_progress)
        mTvDuration = findViewById(R.id.tv_detail_video_duration)
        mIvFull = findViewById(R.id.iv_detail_video_full)

        mTopUI = listOf(mIvGreyTop, mIvBack, mIvFloat)
        mBottomUI = listOf(mIvGreyBottom, mIvPlay, mSeekBar, mTvDuration, mIvFull)

        // 用来做动画
        for (view in mTopUI) {
            view.tag = -100f
        }
        for (view in mBottomUI) {
            view.tag = 100f
        }

        initClick()
        initVideoView()
    }

    private fun initClick() {
        mIvBack.setOnClickListener {
            hideOrShowUI(true)
            detailVideoListener.onBackClick()
        }
        mIvFloat.setOnClickListener { detailVideoListener.onFloatClick() }
        mIvPlay.setOnClickListener {
            if (mIsPlaying) {
                videoView.pause()
            } else {
                videoView.start()
            }
        }
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val duration = (videoView.duration.toFloat() / 1000).toInt()
                    val position = (progress.toFloat() / 1000 * duration).toInt()
                    videoView.seekTo(position * 1000)
                    mTvProgress.text = String.format(
                        resources.getString(R.string.video_play_progress),
                        position / 60,
                        position % 60,
                        duration / 60,
                        duration % 60
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mUserTouch = true
                mTvProgress.visibility = VISIBLE
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mUserTouch = false
                mTvProgress.visibility = GONE
            }
        })
    }

    fun hideOrShowUI(forceHide: Boolean = false) {
        val views = mutableListOf<View>()
        views.addAll(mTopUI)
        views.addAll(mBottomUI)
        for (view in views) {
            if (forceHide) {
                view.visibility = GONE
            } else {
                val target = view.tag as Float
                val curTranslationY = view.translationY
                view.tag = curTranslationY
                val animator = ObjectAnimator.ofFloat(
                    view,
                    "translationY",
                    curTranslationY,
                    target
                )
                animator.duration = 300
                animator.start()
            }
        }
        mLastAnimStartTime = System.currentTimeMillis()
    }

    private fun initVideoView() {
        videoView.initLayoutParams(layoutParams as LayoutParams?)
        videoView.setVideoPlayerListener(object : VideoView.VideoPlayListener {
            override fun onVideoPlayStart() {
                mIsPlaying = true
                mHandler.sendEmptyMessage(TIME_MSG)
                updateUI()
            }

            override fun onVideoPlayPause() {
                mIsPlaying = false
                mHandler.removeCallbacksAndMessages(TIME_MSG)
                updateUI()
            }

            override fun onVideoPlayComplete() {
                mIsPlaying = false
                mHandler.removeCallbacksAndMessages(TIME_MSG)
                updateUI()
                if (!mIsUIShow) {
                    hideOrShowUI()
                }
            }

            override fun onVideoPlayFailed() {
                mIsPlaying = false
                mHandler.removeCallbacksAndMessages(TIME_MSG)
                updateUI()
            }

            override fun onVideoClick() {
                if (System.currentTimeMillis() - mLastAnimStartTime > 300) {
                    hideOrShowUI()
                    mIsUIShow = !mIsUIShow
                    if (mIsUIShow) {
                        mUiCountDown.start()
                    }
                }
            }
        })
        videoView.setVideoInitListener {
            videoView.start()
            mFlLoading.visibility = GONE
        }
        mFlVideoParent.addView(videoView)
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TIME_MSG -> {
                    sendEmptyMessageDelayed(TIME_MSG, 16)
                    updateProgress()
                }
            }
        }
    }

    private fun updateUI() {
        mIvPlay.setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                if (mIsPlaying) {
                    R.drawable.ic_pause
                } else {
                    R.drawable.ic_play
                }
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgress() {
        val position = videoView.currentPosition.toFloat() / 1000
        val bufferPosition = videoView.currentBufferPosition.toFloat() / 1000
        val duration = videoView.duration.toFloat() / 1000
        val progress = (position / duration * 1000).toInt()
        val bufferProgress = (bufferPosition / duration * 1000).toInt()
        if (!mUserTouch) {
            mSeekBar.progress = progress
        }
        mSeekBar.secondaryProgress = bufferProgress
        mTvDuration.text = String.format(
            resources.getString(R.string.video_play_progress),
            (position / 60).toInt(),
            (position % 60).toInt(),
            (duration / 60).toInt(),
            (duration % 60).toInt()
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeMessages(TIME_MSG)
    }

    companion object {
        private const val TIME_MSG = 1
    }

    interface DetailVideoListener {
        fun onBackClick()

        fun onFloatClick()
    }
}