package com.marcelje.hackernews.loader;

import android.app.Activity;
import android.support.v4.content.AsyncTaskLoader;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.api.HackerNewsApi;

import java.util.List;

public class StoriesLoader extends AsyncTaskLoader<HackerNewsResponse<List<Long>>> {

    private final Activity mActivity;
    private final String mType;

    private HackerNewsResponse<List<Long>> mItems;

    /* Runs on a worker thread */
    @Override
    public HackerNewsResponse<List<Long>> loadInBackground() {
        HackerNewsResponse<List<Long>> itemIds = HackerNewsResponse.error("Unknown type");

        if (mActivity.getString(R.string.settings_type_option_top).equals(mType)) {
            itemIds = HackerNewsApi.with(mActivity).getTopStories();
        } else if (mActivity.getString(R.string.settings_type_option_best).equals(mType)) {
            itemIds = HackerNewsApi.with(mActivity).getBestStories();
        } else if (mActivity.getString(R.string.settings_type_option_new).equals(mType)) {
            itemIds = HackerNewsApi.with(mActivity).getNewStories();
        } else if (mActivity.getString(R.string.settings_type_option_show).equals(mType)) {
            itemIds = HackerNewsApi.with(mActivity).getShowStories();
        } else if (mActivity.getString(R.string.settings_type_option_ask).equals(mType)) {
            itemIds = HackerNewsApi.with(mActivity).getAskStories();
        } else if (mActivity.getString(R.string.settings_type_option_jobs).equals(mType)) {
            itemIds = HackerNewsApi.with(mActivity).getJobStories();
        }

        return itemIds;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(HackerNewsResponse<List<Long>> items) {
        if (isReset()) {
            return;
        }

        mItems = items;

        if (isStarted()) {
            super.deliverResult(items);
        }
    }

    public StoriesLoader(Activity activity, String type) {
        super(activity);

        mActivity = activity;
        mType = type;
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
