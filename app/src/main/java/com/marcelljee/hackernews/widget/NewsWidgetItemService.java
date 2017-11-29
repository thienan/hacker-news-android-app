package com.marcelljee.hackernews.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.api.HackerNewsApi;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.loader.AppResponse;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;
import com.marcelljee.hackernews.utils.CollectionUtils;
import com.marcelljee.hackernews.utils.ItemUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import timber.log.Timber;

public class NewsWidgetItemService extends RemoteViewsService {
    private int mAppWidgetId;

    public static Intent createIntent(Context context, int appWidgetId) {
        Intent intent = new Intent(context, NewsWidgetItemService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        return intent;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        extractExtras(intent);
        return new IngredientItemFactory(mAppWidgetId);

    }

    private void extractExtras(Intent intent) {
        if (intent.getExtras() != null) {
            mAppWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    class IngredientItemFactory implements RemoteViewsService.RemoteViewsFactory {
        private List<Item> mItems;
        private final int mAppWidgetId;

        public IngredientItemFactory(int appWidgetId) {
            mAppWidgetId = appWidgetId;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            String newsType = NewsWidgetStorage.loadNewsType(getApplicationContext(), mAppWidgetId);
            int newsCount = NewsWidgetStorage.loadNewsCount(getApplicationContext(), mAppWidgetId);

            if (getString(R.string.news_type_bookmarked).equals(newsType)) {
                mItems = DatabaseDao.getBookmarkedItems(getApplicationContext());
                return;
            }

            AppResponse<List<Long>> itemIds = AppResponse.error("Unknown type");

            if (getString(R.string.news_type_top).equals(newsType)) {
                itemIds = HackerNewsApi.getInstance().getTopStories();
            } else if (getString(R.string.news_type_best).equals(newsType)) {
                itemIds = HackerNewsApi.getInstance().getBestStories();
            } else if (getString(R.string.news_type_new).equals(newsType)) {
                itemIds = HackerNewsApi.getInstance().getNewStories();
            } else if (getString(R.string.news_type_show).equals(newsType)) {
                itemIds = HackerNewsApi.getInstance().getShowStories();
            } else if (getString(R.string.news_type_ask).equals(newsType)) {
                itemIds = HackerNewsApi.getInstance().getAskStories();
            } else if (getString(R.string.news_type_jobs).equals(newsType)) {
                itemIds = HackerNewsApi.getInstance().getJobStories();
            } else if (getString(R.string.news_type_history).equals(newsType)) {
                itemIds = AppResponse.ok(DatabaseDao.getHistoryItems(getApplicationContext()));
            }

            mItems = new ArrayList<>();

            if (itemIds.isSuccessful()) {
                Observable.fromIterable(CollectionUtils.subList(itemIds.getData(), 0, newsCount))
                        .flatMap(itemId -> HackerNewsApi.getInstance().getItem(itemId))
                        .filter(item -> item.isNotDeleted() || item.isNotDead())
                        .toList()
                        .subscribe(data -> mItems.addAll(data), Timber::e);
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public RemoteViews getViewAt(int viewPosition) {
            if (mItems == null || mItems.size() == 0) return null;

            Item item = mItems.get(viewPosition);

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_news_item);
            views.setTextViewText(R.id.tv_user, item.getBy());
            views.setTextViewText(R.id.tv_time, ItemUtils.getRelativeDate(getApplicationContext(), item));
            views.setTextViewText(R.id.tv_text, ItemUtils.getTitle(getApplicationContext(), item.getTitle(), item.getUrl()));
            views.setTextViewText(R.id.tv_score,
                    getResources().getQuantityString(R.plurals.total_scores,
                            item.getScore(), item.getScore()));
            views.setTextViewText(R.id.tv_comment_total,
                    getResources().getQuantityString(R.plurals.total_comments,
                            item.getDescendants(), item.getDescendants()));

            views.setViewVisibility(R.id.tv_comment_total, item.getDescendants() > 0 ? View.VISIBLE : View.GONE);

            Bundle extras = new Bundle();
            extras.putParcelable(ItemActivity.EXTRA_ITEMS, Parcels.wrap(mItems));
            extras.putInt(ItemActivity.EXTRA_ITEM_POSITION, viewPosition);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.news_widget_item, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int itemPosition) {
            return mItems.get(itemPosition).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}