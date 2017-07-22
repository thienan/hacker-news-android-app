package com.marcelje.hackernews.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NewsWidgetConfigureActivity NewsWidgetConfigureActivity}
 */
public class NewsWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        NewsWidgetUpdaterService.startActionWidgetUpdate(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            NewsWidgetStorage.deleteNewsType(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget_preview is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget_preview is disabled
    }
}

