package co.marcelje.hackernews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import co.marcelje.hackernews.R;

public final class SettingsUtils {

    private SettingsUtils() {}

    public static String getDefaultNews(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(context.getString(R.string.settings_type_key),
                context.getString(R.string.settings_type_option_top));
    }

    public static boolean historyEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.settings_history_key), true);
    }

}
