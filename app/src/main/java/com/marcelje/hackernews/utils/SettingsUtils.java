package com.marcelje.hackernews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.marcelje.hackernews.R;

public final class SettingsUtils {

    private SettingsUtils() {}

    public static String getDefaultNews(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(context.getString(R.string.settings_type_key),
                context.getString(R.string.settings_type_option_top));
    }

}
