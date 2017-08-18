package com.marcelljee.hackernews.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;

import com.marcelljee.hackernews.database.DatabaseContract;
import com.marcelljee.hackernews.model.Item;

import java.util.List;

@SuppressWarnings("unused")
public class BookmarkedItemLoader extends AsyncTaskLoader<AppResponse<List<Item>>> {
    private final ForceLoadContentObserver mObserver;

    private Uri mUri;
    private String[] mProjection;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mSortOrder;

    private AppResponse<List<Item>> mItems;
    private CancellationSignal mCancellationSignal;

    /* Runs on a worker thread */
    @Override
    public AppResponse<List<Item>> loadInBackground() {
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
        }
        try {
            Cursor cursor = ContentResolverCompat.query(getContext().getContentResolver(),
                    mUri, mProjection, mSelection, mSelectionArgs, mSortOrder,
                    mCancellationSignal);
            if (cursor != null) {
                try {
                    // Ensure the cursor window is filled.
                    cursor.getCount();
                    cursor.registerContentObserver(mObserver);
                } catch (RuntimeException ex) {
                    cursor.close();
                    throw ex;
                }
            }

            List<Item> items = Item.Factory.fromCursor(cursor);

            for (Item item : items) {
                cursor = ContentResolverCompat.query(getContext().getContentResolver(),
                        DatabaseContract.BookmarkedKidEntry.CONTENT_URI, null,
                        DatabaseContract.BookmarkedKidEntry.COLUMN_ITEM_ID + "=?",
                        new String[]{String.valueOf(item.getId())}, null,
                        mCancellationSignal);

                item.setKids(Item.Factory.kidsFromCursor(cursor));

                cursor = ContentResolverCompat.query(getContext().getContentResolver(),
                        DatabaseContract.BookmarkedPartEntry.CONTENT_URI, null,
                        DatabaseContract.BookmarkedPartEntry.COLUMN_ITEM_ID + "=?",
                        new String[]{String.valueOf(item.getId())}, null,
                        mCancellationSignal);

                item.setParts(Item.Factory.partsFromCursor(cursor));
            }

            return AppResponse.ok(items);
        } finally {
            synchronized (this) {
                mCancellationSignal = null;
            }
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
    public void deliverResult(AppResponse<List<Item>> items) {
        if (isReset()) {
            return;
        }

        mItems = items;

        if (isStarted()) {
            super.deliverResult(items);
        }
    }

    public BookmarkedItemLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mUri = DatabaseContract.BookmarkedItemEntry.CONTENT_URI;
        mProjection = null;
        mSelection = null;
        mSelectionArgs = null;
        mSortOrder = null;
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

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public void setProjection(String[] projection) {
        mProjection = projection;
    }

    public String getSelection() {
        return mSelection;
    }

    public void setSelection(String selection) {
        mSelection = selection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        mSelectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(String sortOrder) {
        mSortOrder = sortOrder;
    }
}
