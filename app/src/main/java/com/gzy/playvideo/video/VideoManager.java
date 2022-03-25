package com.gzy.playvideo.video;

import android.view.ViewGroup;

import com.gzy.playvideo.video.view.VideoView;

import org.jetbrains.annotations.NotNull;


public class VideoManager {

    private static VideoManager instance;

    private VideoView mVideoView;

    private VideoManager() {
    }

    public static VideoManager getInstance() {
        if (instance == null) {
            instance = new VideoManager();
        }
        return instance;
    }

    @NotNull
    public VideoBuilder build(ViewGroup parentView) {
        return new VideoBuilder(parentView);
    }

    public VideoView getVideoView() {
        return mVideoView;
    }

    public void setVideoView(VideoView mVideoView) {
        this.mVideoView = mVideoView;
    }
}