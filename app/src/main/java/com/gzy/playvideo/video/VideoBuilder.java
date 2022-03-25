package com.gzy.playvideo.video;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gzy.playvideo.video.view.ListVideoView;

import org.jetbrains.annotations.NotNull;


public class VideoBuilder {

    private final String TAG = "VideoAdSlot";

    private final Context mContext;

    private final ViewGroup mParentView;

    public VideoBuilder(ViewGroup parentView) {
        mContext = parentView.getContext();
        mParentView = parentView;
    }

    /**
     * 创建视频流 View
     */
    @NotNull
    public ListVideoView createListVideoView() {
        ListVideoView listVideoView = new ListVideoView(mContext);
        mParentView.addView(listVideoView);
        return listVideoView;
    }

    /**
     * 创建视频详情页 View
     */
    public View createVideoView() {
        return new View(mContext);
    }

    /**
     * 创建视频全屏页 View
     */
    public View createFullVideoView() {
        return new View(mContext);
    }
}
