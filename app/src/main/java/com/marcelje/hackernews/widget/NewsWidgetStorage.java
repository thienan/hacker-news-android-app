package com.marcelje.hackernews.widget;

import android.content.Context;
import android.content.SharedPreferences;

class NewsWidgetStorage {

    private static final String INVALID_NEWS_TYPE = "";

    private static final String PREFS_NAME = "com.marcelje.hackernews.widget_preview.NewsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    static void saveNewsType(Context context, int appWidgetId, String newsType) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, newsType);
        prefs.apply();
    }

    static String loadNewsType(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY + appWidgetId, INVALID_NEWS_TYPE);
    }

    static void deleteNewsType(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

}
