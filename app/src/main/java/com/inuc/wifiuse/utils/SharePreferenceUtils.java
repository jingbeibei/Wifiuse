package com.inuc.wifiuse.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 景贝贝 on 2016/7/29.
 */
public class SharePreferenceUtils {
    private static SharedPreferences pref;

    public SharePreferenceUtils(SharedPreferences pref) {
        this.pref = pref;
    }

    public static long getApplicationID() {
     return    pref.getLong("applicationID", 1);

    }
}
