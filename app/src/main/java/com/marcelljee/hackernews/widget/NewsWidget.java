package com.marcelljee.hackernews.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class NewsWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        NewsWidgetUpdaterService.startActionWidgetUpdate(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            NewsWidgetStorage.deleteWidgetConfig(context, appWidgetId);
        }
    }
}

