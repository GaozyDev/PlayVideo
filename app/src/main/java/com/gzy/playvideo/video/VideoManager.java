package com.gzy.playvideo.video;

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
    public VideoBuilder build() {
        return new VideoBuilder();
    }

    public VideoView getVideoView() {
        VideoView videoView = mVideoView;
        mVideoView = null;
        return videoView;
    }

    public void setVideoView(VideoView mVideoView) {
        this.mVideoView = mVideoView;
    }
}