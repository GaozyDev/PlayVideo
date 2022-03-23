package com.gzy.playvideo.video;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gzy.playvideo.video.data.AdParameters;
import com.gzy.playvideo.video.data.SDKConstant;
import com.gzy.playvideo.video.utils.Utils;
import com.gzy.playvideo.video.view.ListVideoView;
import com.gzy.playvideo.video.view.VideoView;


public class VideoEngine implements VideoView.VideoPlayerListener {

    private final String TAG = "VideoAdSlot";

    private Context mContext;

    private ListVideoView mListVideoView;
    private final ViewGroup mParentView;

    private VideoData mVideoData;
    private final VideoListener mVideoEngineListener;
    private int mLastArea = 0;

    public VideoEngine(ViewGroup parentView, VideoData videoData, VideoListener videoEngineListener) {
        mParentView = parentView;
        mContext = mParentView.getContext();
        mVideoData = videoData;
        mVideoEngineListener = videoEngineListener;
        initVideoView();
    }

    private void initVideoView() {
        mListVideoView = new ListVideoView(mContext, mParentView, mVideoData.getUrl(), mVideoData.getPreview());
        mListVideoView.setAdVideoPlayerListener(this);

        RelativeLayout paddingView = new RelativeLayout(mContext);
        paddingView.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
        paddingView.setLayoutParams(mListVideoView.getLayoutParams());
        mParentView.addView(paddingView);
        mParentView.addView(mListVideoView);
    }

    @Override
    public void onVideoLoadSuccess() {
        if (mVideoEngineListener != null) {
            mVideoEngineListener.onAdVideoLoadSuccess();
        }
    }

    @Override
    public void onVideoLoadFailed() {
        if (mVideoEngineListener != null) {
            mVideoEngineListener.onAdVideoLoadFailed();
        }
    }

    private void pauseVideo() {
        mListVideoView.pause();
    }

    private void resumeVideo() {
        mListVideoView.resume();
    }

    private boolean isPlaying() {
        return mListVideoView.isPlaying();
    }

    public void destroy() {
        mListVideoView.destroy();
        mListVideoView = null;
        mContext = null;
        mVideoData = null;
    }

    @Override
    public void onVideoClick() {
        if (mVideoEngineListener != null) {
            mVideoEngineListener.onClickVideo();
        }
    }

    @Override
    public void onVideoPlayComplete() {

    }

    public void updateVideo() {
        mListVideoView.updateVideo();
    }

    public interface VideoListener {

        void onAdVideoLoadSuccess();

        void onAdVideoLoadFailed();

        void onClickVideo();
    }
}
