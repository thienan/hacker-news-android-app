package com.marcelje.hackernews.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.screen.news.NewsActivity;
import com.marcelje.hackernews.screen.news.item.BaseItemActivity;
import com.marcelje.hackernews.screen.news.item.StoryActivity;

public class NewsWidgetUpdaterService extends IntentService {
    private static final String ACTION_WIDGET_UPDATE = "com.marcelje.hackernews.widget_preview.action.WIDGET_UPDATE";

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
        int[] appWidgetIds =
                appWidgetManager.getAppWidgetIds(new ComponentName(this, NewsWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_news_list);

        for (int appWidgetId : appWidgetIds) {
            String newsType = NewsWidgetStorage.loadNewsType(this, appWidgetId);
            int newsCount = NewsWidgetStorage.loadNewsCount(this, appWidgetId);
            if (TextUtils.isEmpty(newsType)) continue;
            updateAppWidget(this, appWidgetManager, appWidgetId, newsType, newsCount);
        }
    }

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId,
                                String newsType,
                                int newsCount) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_news_layout);

        Intent widgetItemIntent = NewsWidgetItemService.createIntent(context, newsType, newsCount);

        views.setRemoteAdapter(R.id.lv_news_list, widgetItemIntent);
        views.setEmptyView(R.id.lv_news_list, R.id.tv_empty);

        views.setTextViewText(R.id.tv_title, newsType);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(NewsActivity.createIntent(context, newsType));
        stackBuilder.addNextIntent(StoryActivity.createIntent(context));

        PendingIntent itemPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.lv_news_list, itemPendingIntent);

        Intent openActivity = NewsActivity.createIntent(context, newsType);

        PendingIntent openActivityPendingIntent =
                PendingIntent.getActivity(context, 0,
                        openActivity,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.news_widget, openActivityPendingIntent);

        Intent refreshWidget = NewsWidgetUpdaterService.createIntent(context);

        PendingIntent refreshWidgetPendingIntent =
                PendingIntent.getService(context, 0,
                        refreshWidget,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.ib_news_refresh, refreshWidgetPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
