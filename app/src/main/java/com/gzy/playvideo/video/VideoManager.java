package com.gzy.playvideo.video;

import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;


public class VideoManager {

    private static VideoManager instance;

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
}