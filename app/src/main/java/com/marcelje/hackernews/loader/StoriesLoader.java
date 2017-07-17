package com.marcelje.hackernews.loader;

import android.app.Activity;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;

import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.screen.news.NewsActivityFragment;

import java.util.List;

public class StoriesLoader extends AsyncTaskLoader<HackerNewsResponse<List<Long>>> {

    private final Activity mActivity;
    private final String mType;

    private HackerNewsResponse<List<Long>> mItems;
    private CancellationSignal mCancellationSignal;

    /* Runs on a worker thread */
    @Override
    public HackerNewsResponse<List<Long>> loadInBackground() {
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
        }

        HackerNewsResponse<List<Long>> itemIds = HackerNewsResponse.error("Unknown type");

        switch (mType) {
            case NewsActivityFragment.TYPE_TOP:
                itemIds = HackerNewsApi.with(mActivity).getTopStories();
                break;
            case NewsActivityFragment.TYPE_BEST:
                itemIds = HackerNewsApi.with(mActivity).getBestStories();
                break;
            case NewsActivityFragment.TYPE_NEW:
                itemIds = HackerNewsApi.with(mActivity).getNewStories();
                break;
            case NewsActivityFragment.TYPE_SHOW:
                itemIds = HackerNewsApi.with(mActivity).getShowStories();
                break;
            case NewsActivityFragment.TYPE_ASK:
                itemIds = HackerNewsApi.with(mActivity).getAskStories();
                break;
            case NewsActivityFragment.TYPE_JOB:
                itemIds = HackerNewsApi.with(mActivity).getJobStories();
                break;
            default:
                break;
        }

        synchronized (this) {
            mCancellationSignal = null;
        }

        if (itemIds.isSuccessful()) {
            return HackerNewsResponse.ok(itemIds.getData());
        } else {
            return HackerNewsResponse.error(itemIds.getErrorMessage());
        }
    }

    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();

        synchronized (this) {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }
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
