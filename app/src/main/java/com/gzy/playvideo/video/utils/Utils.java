package com.gzy.playvideo.video.utils;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gzy.playvideo.video.data.SDKConstant;


public class Utils {

  //is wifi connected
  public static boolean isWifiConnected(Context context) {
    if (context.checkCallingOrSelfPermission(permission.ACCESS_WIFI_STATE)
        != PackageManager.PERMISSION_GRANTED) {
      return false;
    }
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
    if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
      return true;
    }
    return false;
  }

  // decide can autoplay the ad
  public static boolean canAutoPlay(Context context, SDKConstant.AutoPlaySetting setting) {
    boolean result = true;
    switch (setting) {
      case AUTO_PLAY_3G_4G_WIFI:
        result = true;
        break;
      case AUTO_PLAY_ONLY_WIFI:
        if (isWifiConnected(context)) {
          result = true;
        } else {
          result = false;
        }
        break;
      case AUTO_PLAY_NEVER:
        result = false;
        break;
    }
    return result;
  }
}
