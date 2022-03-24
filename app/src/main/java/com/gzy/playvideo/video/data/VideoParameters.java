package com.gzy.playvideo.video.data;

public final class VideoParameters {

    private static final SDKConstant.AutoPlaySetting currentSetting = SDKConstant.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI;

    public static SDKConstant.AutoPlaySetting getCurrentSetting() {
        return currentSetting;
    }
}
