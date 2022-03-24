package com.gzy.playvideo.video;

import android.content.Context;
import android.view.ViewGroup;

import com.gzy.playvideo.video.data.VideoData;
import com.gzy.playvideo.video.view.ListVideoView;


public class VideoBuilder {

    private final String TAG = "VideoAdSlot";

    private final Context mContext;

    private final ViewGroup mParentView;

    public VideoBuilder(ViewGroup parentView) {
        mContext = parentView.getContext();
        mParentView = parentView;
    }

    public ListVideoView createListVideoView() {
        ListVideoView listVideoView = new ListVideoView(mContext, mParentView);
        mParentView.addView(listVideoView);
        return listVideoView;
    }
}
