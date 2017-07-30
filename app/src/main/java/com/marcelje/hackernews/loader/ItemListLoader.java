package com.marcelje.hackernews.loader;

import android.app.Activity;
import android.support.v4.content.AsyncTaskLoader;

import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.model.Item;

import java.util.List;

import io.reactivex.Observable;

public class ItemListLoader extends AsyncTaskLoader<HackerNewsResponse<List<Item>>> {

    private final Activity mActivity;
    private final List<Long> mItemIds;

    private HackerNewsResponse<List<Item>> mItems;

    /* Runs on a worker thread */
    @Override
    public HackerNewsResponse<List<Item>> loadInBackground() {
        final HackerNewsResponse[] items = new HackerNewsResponse[1];

        Observable.fromIterable(mItemIds)
                .flatMap(itemId -> HackerNewsApi.with(mActivity).getItem(itemId))
                .toList()
                .subscribe(data -> items[0] = HackerNewsResponse.ok(data));

        return items[0];
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(HackerNewsResponse<List<Item>> items) {
        if (isReset()) {
            return;
        }

        mItems = items;

        if (isStarted()) {
            super.deliverResult(items);
        }
    }

    public ItemListLoader(Activity activity, List<Long> itemIds) {
        super(activity);

        mActivity = activity;
        mItemIds = itemIds;
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (mItems != null) {
            deliverResult(mItems);
        }
        if (takeContentChanged() || mItems == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        mItems = null;
    }
}
