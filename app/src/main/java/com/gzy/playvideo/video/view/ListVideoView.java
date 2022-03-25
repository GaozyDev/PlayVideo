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
import com.gzy.playvideo.video.data.SDKConstant;
import com.gzy.playvideo.video.utils.DisplayUtil;

@SuppressLint("ViewConstructor")
public class ListVideoView extends FrameLayout {

    private final ViewGroup mParentView;

    private FrameLayout mFlVideoContent;

    private ImageView mIvPlay;

    private VideoView mVideoView;

    private boolean mIsMute = true;

    private ListVideoListener mListVideoListener;

    public ListVideoView(Context context, ViewGroup parentView) {
        super(context);
        mParentView = parentView;
        initView();
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels - DisplayUtil.dp2px(getContext(), 20);
        int height = (int) (width * SDKConstant.VIDEO_HEIGHT_PERCENT);
        LayoutParams layoutParams = new LayoutParams(width, height);
        setLayoutParams(layoutParams);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_list_video, this);

        mFlVideoContent = findViewById(R.id.fl_list_video_content);
        mIvPlay = findViewById(R.id.iv_list_video_play);
        ImageView mIvFullScreen = findViewById(R.id.iv_list_video_full);
        ImageView mIvBarrage = findViewById(R.id.iv_list_video_barrage);
        ImageView mIvMute = findViewById(R.id.iv_list_video_mute);

        initVideoView(layoutParams);

        mIvPlay.setOnClickListener(v -> {
        });
        mIvMute.setOnClickListener(v -> {
            mIsMute = !mIsMute;
            mVideoView.mute(mIsMute);
        });
    }

    private void initVideoView(LayoutParams layoutParams) {
        mVideoView = new VideoView(getContext(), layoutParams);
        mVideoView.setVideoPlayerListener(new VideoView.VideoPlayListener() {
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
                    mListVideoListener.onVideoClick(mVideoView);
                }
            }
        });
        mFlVideoContent.addView(mVideoView);
        mVideoView.mute(mIsMute);
    }

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

    public void setVideoInitListener(VideoView.VideoInitListener mVideoInitListener) {
        mVideoView.setVideoInitListener(mVideoInitListener);
    }

    public VideoView getVideoView() {
        return mVideoView;
    }

    public void setListVideoListener(ListVideoListener listVideoListener) {
        this.mListVideoListener = listVideoListener;
    }

    public interface ListVideoListener {
        void onVideoClick(VideoView videoView);
    }
}