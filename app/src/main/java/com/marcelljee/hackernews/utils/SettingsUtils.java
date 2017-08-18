package com.marcelljee.hackernews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.marcelljee.hackernews.R;

public final class SettingsUtils {

    private SettingsUtils() {}

    public static String getDefaultNews(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(context.getString(R.string.settings_type_key),
                context.getString(R.string.news_type_top));
    }

    public static boolean readIndicatorEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.settings_read_indicator_key), true);
    }

    public static boolean historyEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.settings_history_key), true);
    }

}
