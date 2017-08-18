package com.marcelljee.hackernews.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.marcelljee.hackernews.api.HackerNewsApi;
import com.marcelljee.hackernews.model.Item;

import java.util.List;

import io.reactivex.Observable;
import timber.log.Timber;

public class ItemListLoader extends AsyncTaskLoader<AppResponse<List<Item>>> {

    private final List<Long> mItemIds;

    private AppResponse<List<Item>> mItems;

    /* Runs on a worker thread */
    @Override
    public AppResponse<List<Item>> loadInBackground() {
        final AppResponse[] items = new AppResponse[1];

        Observable.fromIterable(mItemIds)
                .flatMap(itemId -> HackerNewsApi.getInstance().getItem(itemId))
                .toList()
                .subscribe(data -> items[0] = AppResponse.ok(data), Timber::e);

        return items[0];
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(AppResponse<List<Item>> items) {
        if (isReset()) {
            return;
        }

        mItems = items;

        if (isStarted()) {
            super.deliverResult(items);
        }
    }

    public ItemListLoader(Context context, List<Long> itemIds) {
        super(context);
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
