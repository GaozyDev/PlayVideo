package com.gzy.playvideo.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gzy.playvideo.R;
import com.gzy.playvideo.video.data.SDKConstant;
import com.gzy.playvideo.video.utils.DisplayUtil;

import org.jetbrains.annotations.NotNull;

@SuppressLint("ViewConstructor")
public class ListVideoView extends FrameLayout {


    private FrameLayout mFlVideoParent;

    private ImageView mIvPlay;

    private VideoView mVideoView;

    private boolean mIsMute = true;

    private LayoutParams mLayoutParams;

    private ListVideoListener mListVideoListener;

    public ListVideoView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels - DisplayUtil.dp2px(getContext(), 20);
        int height = (int) (width * SDKConstant.VIDEO_HEIGHT_PERCENT);
        mLayoutParams = new LayoutParams(width, height);
        setLayoutParams(mLayoutParams);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_list_video, this);

        mFlVideoParent = findViewById(R.id.fl_list_video_parent);
        mIvPlay = findViewById(R.id.iv_list_video_play);
        ImageView mIvFullScreen = findViewById(R.id.iv_list_video_full);
        ImageView mIvBarrage = findViewById(R.id.iv_list_video_barrage);
        ImageView mIvMute = findViewById(R.id.iv_list_video_mute);

        initVideoView(mLayoutParams);

        mIvPlay.setOnClickListener(v -> {
        });
        mIvMute.setOnClickListener(v -> {
            mIsMute = !mIsMute;
            mVideoView.mute(mIsMute);
        });
    }

    private void initVideoView(LayoutParams layoutParams) {
        mVideoView = new VideoView(getContext(), layoutParams, true);
        mVideoView.setVideoPlayerListener(videoPlayListener);
        mFlVideoParent.addView(mVideoView);
        mVideoView.mute(mIsMute);
    }

    VideoView.VideoPlayListener videoPlayListener = new VideoView.VideoPlayListener() {
        @Override
        public void onVideoPlayStart() {
            mIvPlay.setVisibility(GONE);
        }

        @Override
        public void onVideoPlayFailed() {

        }

        @Override
        public void onVideoPlayComplete() {
            mIvPlay.setVisibility(VISIBLE);
        }

        @Override
        public void onVideoClick() {
            if (mListVideoListener != null) {
                mListVideoListener.onVideoClick(ListVideoView.this);
            }
        }
    };

    public void resume() {
        mVideoView.start();
    }

    public void pause() {
        mVideoView.pause();
    }

    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    public void loadPreview(String url) {
        mIvPlay.setVisibility(VISIBLE);
        mVideoView.loadPreview(url);
    }

    public void loadVideo(String url) {
        mVideoView.loadVideo(url);
    }

    public void setVideoInitListener(VideoView.VideoInitListener videoInitListener) {
        mVideoView.setVideoInitListener(videoInitListener);
    }

    public VideoView getVideoView() {
        return mVideoView;
    }

    public void setListVideoListener(ListVideoListener listVideoListener) {
        this.mListVideoListener = listVideoListener;
    }

    public void backVideoView(VideoView videoView) {
        mVideoView = videoView;
        mVideoView.setLayoutParams(mLayoutParams);
        mVideoView.setVideoPlayerListener(videoPlayListener);
        mVideoView.setVideoInitListener(VideoView::start);
        mVideoView.setIsProcessDetached(true);
        mFlVideoParent.addView(mVideoView);
    }

    public interface ListVideoListener {
        void onVideoClick(@NotNull ListVideoView listVideoView);
    }
}