package com.marcelljee.hackernews.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.api.HackerNewsApi;

import java.util.List;

public class StoriesLoader extends AsyncTaskLoader<AppResponse<List<Long>>> {

    private final String mNewsType;

    private AppResponse<List<Long>> mItems;

    /* Runs on a worker thread */
    @Override
    public AppResponse<List<Long>> loadInBackground() {
        AppResponse<List<Long>> itemIds = AppResponse.error("Unknown type");

        if (getContext().getString(R.string.news_type_top).equals(mNewsType)) {
            itemIds = HackerNewsApi.getInstance().getTopStories();
        } else if (getContext().getString(R.string.news_type_best).equals(mNewsType)) {
            itemIds = HackerNewsApi.getInstance().getBestStories();
        } else if (getContext().getString(R.string.news_type_new).equals(mNewsType)) {
            itemIds = HackerNewsApi.getInstance().getNewStories();
        } else if (getContext().getString(R.string.news_type_show).equals(mNewsType)) {
            itemIds = HackerNewsApi.getInstance().getShowStories();
        } else if (getContext().getString(R.string.news_type_ask).equals(mNewsType)) {
            itemIds = HackerNewsApi.getInstance().getAskStories();
        } else if (getContext().getString(R.string.news_type_jobs).equals(mNewsType)) {
            itemIds = HackerNewsApi.getInstance().getJobStories();
        }

        return itemIds;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(AppResponse<List<Long>> items) {
        if (isReset()) {
            return;
        }

        mItems = items;

        if (isStarted()) {
            super.deliverResult(items);
        }
    }

    public StoriesLoader(Context context, String newsType) {
        super(context);
        mNewsType = newsType;
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
