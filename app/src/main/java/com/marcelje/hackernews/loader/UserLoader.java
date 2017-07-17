package com.marcelje.hackernews.loader;

import android.app.Activity;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;

import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.model.User;

public class UserLoader extends AsyncTaskLoader<HackerNewsResponse<User>> {

    private final Activity mActivity;
    private final String mUserId;

    private HackerNewsResponse<User> mUser;
    private CancellationSignal mCancellationSignal;

    /* Runs on a worker thread */
    @Override
    public HackerNewsResponse<User> loadInBackground() {
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
        }

        if (mCancellationSignal.isCanceled()) {
            return null;
        }

        HackerNewsResponse<User> user = HackerNewsApi.with(mActivity).getUser(mUserId);

        synchronized (this) {
            mCancellationSignal = null;
        }

        if (user.isSuccessful()) {
            return HackerNewsResponse.ok(user);
        } else {
            return HackerNewsResponse.error(user.getErrorMessage());
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
    public void deliverResult(HackerNewsResponse<User> user) {
        if (isReset()) {
            return;
        }

        mUser = user;

        if (isStarted()) {
            super.deliverResult(user);
        }
    }

    public UserLoader(Activity activity, String userId) {
        super(activity);

        mActivity = activity;
        mUserId = userId;
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
        if (mUser != null) {
            deliverResult(mUser);
        }
        if (takeContentChanged() || mUser == null) {
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

        mUser = null;
    }
}
