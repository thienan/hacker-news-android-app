package com.marcelljee.hackernews.database;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.marcelljee.hackernews.utils.ParcelUtils;
import timber.log.Timber;

public class DatabaseUpdaterService extends IntentService {

    private static final String ACTION_INSERT = "com.marcelljee.hackernews.database.action.INSERT";
    private static final String ACTION_BULK_INSERT = "com.marcelljee.hackernews.database.action.BULK_INSERT";
    private static final String ACTION_DELETE = "com.marcelljee.hackernews.database.action.DELETE";

    private static final String EXTRA_VALUES = "com.marcelljee.hackernews.database.extra.VALUES";
    private static final String EXTRA_WHERE = "com.marcelljee.hackernews.database.extra.WHERE";
    private static final String EXTRA_ARGS = "com.marcelljee.hackernews.database.extra.ARGS";

    public static void startActionInsert(@NonNull Context context, @NonNull Uri uri,
                                         @NonNull ContentValues values) {
        Intent intent = new Intent(context, DatabaseUpdaterService.class);
        intent.setAction(ACTION_INSERT);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void startActionBulkInsert(@NonNull Context context, @NonNull Uri uri,
                                             @NonNull ContentValues[] values) {
        Intent intent = new Intent(context, DatabaseUpdaterService.class);
        intent.setAction(ACTION_BULK_INSERT);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void startActionDelete(@NonNull Context context, @NonNull Uri uri,
                                         @Nullable String where, @Nullable String[] selectionArgs) {
        Intent intent = new Intent(context, DatabaseUpdaterService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        intent.putExtra(EXTRA_WHERE, where);
        intent.putExtra(EXTRA_ARGS, selectionArgs);
        context.startService(intent);
    }

    public DatabaseUpdaterService() {
        super("DatabaseUpdaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSERT.equals(action)) {
                final Uri uri = intent.getData();
                final ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
                handleActionInsert(uri, values);
            } else if (ACTION_BULK_INSERT.equals(action)) {
                final Uri uri = intent.getData();
                ContentValues[] values =
                        ParcelUtils.unwrapToContentValues(intent.getParcelableArrayExtra(EXTRA_VALUES));
                handleActionBulkInsert(uri, values);
            } else if (ACTION_DELETE.equals(action)) {
                final Uri uri = intent.getData();
                final String where = intent.getStringExtra(EXTRA_WHERE);
                final String[] selectionArgs = intent.getStringArrayExtra(EXTRA_ARGS);
                handleActionDelete(uri, where, selectionArgs);
            }
        }
    }

    private void handleActionInsert(@NonNull Uri uri,
                                    @NonNull ContentValues values) {
        if (getContentResolver().insert(uri, values) != null) {
            Timber.d("Inserted new item");
        } else {
            Timber.w("Error inserting new item");
        }
    }

    private void handleActionBulkInsert(@NonNull Uri uri,
                                        @NonNull ContentValues[] values) {
        int count = getContentResolver().bulkInsert(uri, values);
        Timber.d("Inserted " + count + " items");
    }

    private void handleActionDelete(@NonNull Uri uri,
                                    @Nullable String where, @Nullable String[] selectionArgs) {
        int count = getContentResolver().delete(uri, where, selectionArgs);
        Timber.d("Deleted " + count + " items");
    }
}
