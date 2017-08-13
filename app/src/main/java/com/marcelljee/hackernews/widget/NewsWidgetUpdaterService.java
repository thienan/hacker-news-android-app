package com.marcelljee.hackernews.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.marcelljee.hackernews.screen.news.NewsActivity;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;

public class NewsWidgetUpdaterService extends IntentService {
    private static final String ACTION_WIDGET_UPDATE = "com.marcelljee.hackernews.widget.action.WIDGET_UPDATE";

    public NewsWidgetUpdaterService() {
        super("NewsWidgetUpdaterService");
    }

    public static void startActionWidgetUpdate(Context context) {
        Intent intent = createIntent(context);
        context.startService(intent);
    }

    private static Intent createIntent(Context context) {
        Intent intent = new Intent(context, NewsWidgetUpdaterService.class);
        intent.setAction(ACTION_WIDGET_UPDATE);

        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WIDGET_UPDATE.equals(action)) {
                handleActionWidgetUpdate();
            }
        }
    }

    private void handleActionWidgetUpdate() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewsWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, com.marcelljee.hackernews.R.id.lv_news_list);

        for (int appWidgetId : appWidgetIds) {
            String newsType = NewsWidgetStorage.loadNewsType(this, appWidgetId);
            if (TextUtils.isEmpty(newsType)) continue;

            updateAppWidget(this, appWidgetManager, appWidgetId, newsType);
        }
    }

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId,
                                String newsType) {
        RemoteViews views = new RemoteViews(context.getPackageName(), com.marcelljee.hackernews.R.layout.widget_news_layout);
        views.setTextViewText(com.marcelljee.hackernews.R.id.tv_title, newsType);

        setItemList(context, views, appWidgetId);
        setItemClick(context, views, newsType);
        setWidgetClick(context, views, newsType);
        setRefreshClick(context, views);
        setConfigClick(context, views, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setItemList(Context context, RemoteViews views, int appWidgetId) {
        Intent widgetItemIntent = NewsWidgetItemService.createIntent(context, appWidgetId);

        views.setRemoteAdapter(com.marcelljee.hackernews.R.id.lv_news_list, widgetItemIntent);
        views.setEmptyView(com.marcelljee.hackernews.R.id.lv_news_list, com.marcelljee.hackernews.R.id.tv_empty);
    }

    private static void setItemClick(Context context, RemoteViews views, String newsType) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(NewsActivity.createIntent(context, newsType));
        stackBuilder.addNextIntent(ItemActivity.StoryActivity.createIntent(context));

        PendingIntent itemPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(com.marcelljee.hackernews.R.id.lv_news_list, itemPendingIntent);
    }

    private static void setWidgetClick(Context context, RemoteViews views, String newsType) {
        Intent openActivity = NewsActivity.createIntent(context, newsType);

        PendingIntent openActivityPendingIntent =
                PendingIntent.getActivity(context, 0,
                        openActivity,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(com.marcelljee.hackernews.R.id.news_widget, openActivityPendingIntent);
    }

    private static void setRefreshClick(Context context, RemoteViews views) {
        Intent refreshWidget = NewsWidgetUpdaterService.createIntent(context);

        PendingIntent refreshWidgetPendingIntent =
                PendingIntent.getService(context, 0,
                        refreshWidget,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(com.marcelljee.hackernews.R.id.ib_news_refresh, refreshWidgetPendingIntent);
    }

    private static void setConfigClick(Context context, RemoteViews views, int appWidgetId) {
        Intent configWidget = NewsWidgetConfigureActivity.createIntent(context, appWidgetId);

        PendingIntent configWidgetPendingIntent =
                PendingIntent.getActivity(context, 0,
                        configWidget,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(com.marcelljee.hackernews.R.id.ib_news_config, configWidgetPendingIntent);
    }
}
