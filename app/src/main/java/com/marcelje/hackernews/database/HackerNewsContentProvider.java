package com.marcelje.hackernews.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class HackerNewsContentProvider extends ContentProvider {

    private static final String TYPE_DIRECTORY = "vnd.android.cursor.dir";
    private static final String TYPE_ITEM = "vnd.android.cursor.item";

    private static final int BOOKMARKED_ITEM = 100;
    private static final int BOOKMARKED_ITEM_ID = 101;
    private static final int USER = 200;
    private static final int USER_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(HackerNewsContract.AUTHORITY, HackerNewsContract.PATH_BOOKMARKED_ITEMS, BOOKMARKED_ITEM);
        uriMatcher.addURI(HackerNewsContract.AUTHORITY, HackerNewsContract.PATH_BOOKMARKED_ITEMS + "/#", BOOKMARKED_ITEM_ID);
        uriMatcher.addURI(HackerNewsContract.AUTHORITY, HackerNewsContract.PATH_USERS, USER);
        uriMatcher.addURI(HackerNewsContract.AUTHORITY, HackerNewsContract.PATH_USERS + "/#", USER_ID);

        return uriMatcher;
    }

    private HackerNewsDbHelper helper;

    @Override
    public boolean onCreate() {
        helper = new HackerNewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        final SQLiteDatabase db = helper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        String tableName;

        switch (match) {
            case BOOKMARKED_ITEM:
                tableName = HackerNewsContract.BookmarkedItemEntry.TABLE_NAME;
                break;
            case USER:
                tableName = HackerNewsContract.UserEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor = db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        if (getContext() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      ContentValues values) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        String tableName;
        Uri contentUri;

        switch (match) {
            case BOOKMARKED_ITEM:
                tableName = HackerNewsContract.BookmarkedItemEntry.TABLE_NAME;
                contentUri = HackerNewsContract.BookmarkedItemEntry.CONTENT_URI;
                break;
            case USER:
                tableName = HackerNewsContract.UserEntry.TABLE_NAME;
                contentUri = HackerNewsContract.UserEntry.CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        long id = db.insert(tableName, null, values);

        if (id > 0) {
            returnUri = ContentUris.withAppendedId(contentUri, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        String tableName;

        switch (match) {
            case BOOKMARKED_ITEM:
                tableName = HackerNewsContract.BookmarkedItemEntry.TABLE_NAME;
                break;
            case USER:
                tableName = HackerNewsContract.UserEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, valuesArray);
        }

        db.beginTransaction();
        int inserted = 0;

        for (ContentValues values : valuesArray) {
            long id = db.insert(tableName, null, values);
            if (id > 0) inserted++;
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        if (inserted > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return inserted;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int deleted;

        String id = uri.getPathSegments().get(1);
        String tableName;

        switch (match) {
            case BOOKMARKED_ITEM_ID:
                tableName = HackerNewsContract.BookmarkedItemEntry.TABLE_NAME;
                break;
            case USER_ID:
                tableName = HackerNewsContract.UserEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        deleted = db.delete(tableName, BaseColumns._ID + "=?", new String[]{id});

        if (deleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int updated;

        String id = uri.getPathSegments().get(1);
        String tableName;

        switch (match) {
            case BOOKMARKED_ITEM_ID:
                tableName = HackerNewsContract.BookmarkedItemEntry.TABLE_NAME;
                break;
            case USER_ID:
                tableName = HackerNewsContract.UserEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        updated = db.update(tableName, values, BaseColumns._ID + "=?", new String[]{id});

        if (updated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKMARKED_ITEM:
                return TYPE_DIRECTORY + "/" +
                        HackerNewsContract.AUTHORITY + "/" +
                        HackerNewsContract.PATH_BOOKMARKED_ITEMS;
            case BOOKMARKED_ITEM_ID:
                return TYPE_ITEM + "/" +
                        HackerNewsContract.AUTHORITY + "/" +
                        HackerNewsContract.PATH_BOOKMARKED_ITEMS;
            case USER:
                return TYPE_DIRECTORY + "/" +
                        HackerNewsContract.AUTHORITY + "/" +
                        HackerNewsContract.PATH_USERS;
            case USER_ID:
                return TYPE_ITEM + "/" +
                        HackerNewsContract.AUTHORITY + "/" +
                        HackerNewsContract.PATH_USERS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
