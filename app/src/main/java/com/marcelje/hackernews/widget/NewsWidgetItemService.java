package com.marcelje.hackernews.widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.ItemActivity;
import com.marcelje.hackernews.utils.CollectionUtils;
import com.marcelje.hackernews.utils.ItemUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class NewsWidgetItemService extends RemoteViewsService {

    public static final String EXTRA_NEWS_TYPE = "com.marcelje.hackernews.widget_preview.extra.NEWS_TYPE";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (intent.hasExtra(EXTRA_NEWS_TYPE)) {
            return new IngredientItemFactory(intent.getStringExtra(EXTRA_NEWS_TYPE));
        }

        return null;
    }

    class IngredientItemFactory implements RemoteViewsService.RemoteViewsFactory {

        private static final int MAX_ITEMS_SHOWN = 20;

        private List<Item> mItems;
        private final String mNewsType;

        public IngredientItemFactory(String newsType) {
            mNewsType = newsType;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            HackerNewsResponse<List<Long>> itemIds = HackerNewsResponse.error("Unknown type");

            if (getString(R.string.settings_type_option_bookmarked).equals(mNewsType)) {
                mItems = HackerNewsDao.getItems(getApplicationContext());
                return;
            }

            if (getString(R.string.settings_type_option_top).equals(mNewsType)) {
                itemIds = HackerNewsApi.with(getApplication()).getTopStories();
            } else if (getString(R.string.settings_type_option_best).equals(mNewsType)) {
                itemIds = HackerNewsApi.with(getApplication()).getBestStories();
            } else if (getString(R.string.settings_type_option_new).equals(mNewsType)) {
                itemIds = HackerNewsApi.with(getApplication()).getNewStories();
            } else if (getString(R.string.settings_type_option_show).equals(mNewsType)) {
                itemIds = HackerNewsApi.with(getApplication()).getShowStories();
            } else if (getString(R.string.settings_type_option_ask).equals(mNewsType)) {
                itemIds = HackerNewsApi.with(getApplication()).getAskStories();
            } else if (getString(R.string.settings_type_option_jobs).equals(mNewsType)) {
                itemIds = HackerNewsApi.with(getApplication()).getJobStories();
            }

            mItems = new ArrayList<>();

            if (itemIds.isSuccessful()) {
                for (long itemId : CollectionUtils.subList(itemIds.getData(), 0, MAX_ITEMS_SHOWN)) {
                    HackerNewsResponse<Item> itemResponse = HackerNewsApi.with(getApplication()).getItem(itemId);
                    if (itemResponse.isSuccessful()) {
                        Item item = itemResponse.getData();
                        if (item.isNotDeleted() && item.isNotDead()) {
                            mItems.add(itemResponse.getData());
                        }
                    }
                }
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
        public RemoteViews getViewAt(int i) {
            if (mItems == null || mItems.size() == 0) return null;

            Item item = mItems.get(i);

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_news_item);
            views.setTextViewText(R.id.tv_user, item.getBy());
            views.setTextViewText(R.id.tv_time, ItemUtils.getRelativeDate(getApplicationContext(), item));
            views.setTextViewText(R.id.tv_text, ItemUtils.getTitle(getApplicationContext(), item));
            views.setTextViewText(R.id.tv_score,
                    getResources().getQuantityString(R.plurals.total_scores,
                            item.getScore(), item.getScore()));
            views.setTextViewText(R.id.tv_comment_total,
                    getResources().getQuantityString(R.plurals.total_comments,
                            item.getDescendants(), item.getDescendants()));

            views.setViewVisibility(R.id.tv_comment_total, item.getDescendants() > 0 ? View.VISIBLE : View.GONE);

            Bundle extras = new Bundle();
            extras.putParcelable(ItemActivity.EXTRA_ITEM, Parcels.wrap(item));
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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}