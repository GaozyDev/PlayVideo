package com.gzy.playvideo.video;

import android.view.ViewGroup;


public class VideoContext implements VideoEngine.VideoListener {

    private final VideoEngine mVideoEngine;
    private VideoContextInterface mListener;

    public VideoContext(ViewGroup parentView, VideoData videoData) {
        mVideoEngine = new VideoEngine(parentView, videoData, this);
    }

    public void setResultListener(VideoContextInterface listener) {
        this.mListener = listener;
    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mListener != null) {
            mListener.onSuccess();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {
        if (mListener != null) {
            mListener.onFailed();
        }
    }

    @Override
    public void onClickVideo() {
        if (mListener != null) {
            mListener.onClickVideo();
        }
    }

    public void updateVideo() {
        if (mVideoEngine != null) {
            mVideoEngine.updateVideo();
        }
    }

    public void destroy() {
        mVideoEngine.destroy();
    }
}