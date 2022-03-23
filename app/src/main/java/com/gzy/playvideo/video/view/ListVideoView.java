package com.gzy.playvideo.video.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gzy.playvideo.R;
import com.gzy.playvideo.video.data.AdParameters;
import com.gzy.playvideo.video.data.SDKConstant;
import com.gzy.playvideo.video.utils.DisplayUtil;
import com.gzy.playvideo.video.utils.Utils;

@SuppressLint("ViewConstructor")
public class ListVideoView extends FrameLayout {

    private final ViewGroup mParentView;

    private FrameLayout mFlVideoContent;

    private ImageView mIvFullScreen;

    private ImageView mIvBarrage;

    private ImageView mIvMute;

    private VideoView mVideoView;

    private final String mUrl;

    private final String mPreviewURL;

    private boolean mIsMute = true;

    private int mLastArea;

    public ListVideoView(Context context, ViewGroup parentView, String sourceUrl, String previewUrl) {
        super(context);
        mParentView = parentView;
        mUrl = sourceUrl;
        mPreviewURL = previewUrl;
        initView();
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels - DisplayUtil.dp2px(getContext(), 20);
        int height = (int) (width * SDKConstant.VIDEO_HEIGHT_PERCENT);
        LayoutParams params = new LayoutParams(width, height);
        setLayoutParams(params);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_list_video, this);

        mFlVideoContent = findViewById(R.id.fl_list_video_content);
        mIvFullScreen = findViewById(R.id.iv_list_video_full);
        mIvBarrage = findViewById(R.id.iv_list_video_barrage);
        mIvMute = findViewById(R.id.iv_list_video_mute);

        initVideoView(params);

        mIvMute.setOnClickListener(v -> {
            mIsMute = !mIsMute;
            mVideoView.mute(mIsMute);
        });
    }

    private void initVideoView(LayoutParams params) {
        mVideoView = new VideoView(getContext(), mParentView, params, mUrl, mPreviewURL);
        mVideoView.setVideoPlayerListener(new VideoView.VideoPlayerListener() {
            @Override
            public void onVideoLoadSuccess() {

            }

            @Override
            public void onVideoLoadFailed() {

            }

            @Override
            public void onVideoPlayComplete() {

            }

            @Override
            public void onVideoClick() {

            }
        });
        mFlVideoContent.addView(mVideoView);
        mVideoView.mute(mIsMute);
    }

    public void resume() {
        mVideoView.resume();
    }

    public void pause() {
        mVideoView.pause();
    }

    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    public void setAdVideoPlayerListener(VideoView.VideoPlayerListener videoPlayerListener) {
        mVideoView.setVideoPlayerListener(videoPlayerListener);
    }

    public void updateVideo() {
        int currentArea = Utils.getVisiblePercent(mParentView);
        // 小于0表示未出现在屏幕上，不做任何处理
        if (currentArea <= 0) {
            return;
        }

        if (Math.abs(currentArea - mLastArea) >= 100) {
            return;
        }

        if (currentArea < SDKConstant.VIDEO_SCREEN_PERCENT) {
            mLastArea = 0;
            return;
        }

        // 满足自动播放条件或者用户主动点击播放，开始播放
        if (Utils.canAutoPlay(mParentView.getContext(), AdParameters.getCurrentSetting())
                || isPlaying()) {
            mLastArea = currentArea;
            mVideoView.resume();
        } else {
            mVideoView.pause();
        }
    }

    public void destroy() {
        mVideoView.destroy();
    }
}