package com.marcelje.hackernews.widget;

import android.content.Context;
import android.content.SharedPreferences;

class NewsWidgetStorage {

    private static final String INVALID_NEWS_TYPE = "";
    private static final int NEWS_COUNT_DEFAULT = 20;

    private static final String PREFS_NAME = "com.marcelje.hackernews.widget_preview.NewsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String NEWS_TYPE = "_news_type";
    private static final String NEWS_COUNT = "_news_count";

    static void saveWidgetConfig(Context context, int appWidgetId, String newsType, int newsCount) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + NEWS_TYPE, newsType);
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId + NEWS_COUNT, newsCount);
        prefs.apply();
    }

    static String loadNewsType(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY + appWidgetId + NEWS_TYPE, INVALID_NEWS_TYPE);
    }

    static int loadNewsCount(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId + NEWS_COUNT, NEWS_COUNT_DEFAULT);
    }

    static void deleteWidgetConfig(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + NEWS_TYPE);
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + NEWS_COUNT);
        prefs.apply();
    }

}
