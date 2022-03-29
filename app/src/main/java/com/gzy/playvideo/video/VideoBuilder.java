package com.gzy.playvideo.video;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gzy.playvideo.video.view.DetailVideoView;
import com.gzy.playvideo.video.view.ListVideoView;
import com.gzy.playvideo.video.view.VideoView;

import org.jetbrains.annotations.NotNull;


public class VideoBuilder {

    private final String TAG = "VideoAdSlot";

    private Context mContext;

    private ViewGroup mParentView;

    public VideoBuilder() {
    }

    public VideoBuilder setParentView(ViewGroup parentView) {
        mContext = parentView.getContext();
        mParentView = parentView;
        return this;
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
    public DetailVideoView createDetailVideoView(@NotNull VideoView videoView,
                                                 @NotNull DetailVideoView.DetailVideoListener detailVideoListener) {
        DetailVideoView detailVideoView = new DetailVideoView(mContext, videoView, detailVideoListener);
        mParentView.addView(detailVideoView);
        return detailVideoView;
    }

    /**
     * 创建视频全屏页 View
     */
    public View createFullVideoView() {
        return new View(mContext);
    }
}
