package co.marcelje.hackernews.loader;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import co.marcelje.hackernews.api.HackerNewsApi;
import co.marcelje.hackernews.model.User;

public class UserLoader extends AsyncTaskLoader<HackerNewsResponse<User>> {

    private final String mUserId;

    private HackerNewsResponse<User> mUser;

    /* Runs on a worker thread */
    @Override
    public HackerNewsResponse<User> loadInBackground() {
        return HackerNewsApi.getInstance().getUser(mUserId);
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

    public UserLoader(Context context, String userId) {
        super(context);
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
