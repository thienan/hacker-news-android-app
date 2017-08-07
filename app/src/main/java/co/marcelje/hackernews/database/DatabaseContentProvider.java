package co.marcelje.hackernews.database;

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

public class DatabaseContentProvider extends ContentProvider {

    private static final String TYPE_DIRECTORY = "vnd.android.cursor.dir";
    private static final String TYPE_ITEM = "vnd.android.cursor.item";

    private static final int BOOKMARKED_ITEM = 100;
    private static final int BOOKMARKED_ITEM_ID = 101;
    private static final int BOOKMARKED_KID = 200;
    private static final int BOOKMARKED_KID_ID = 201;
    private static final int BOOKMARKED_PART = 300;
    private static final int BOOKMARKED_PART_ID = 301;
    private static final int READ_HISTORY = 400;
    private static final int READ_HISTORY_ID = 401;
    private static final int USER = 500;
    private static final int USER_ID = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_BOOKMARKED_ITEMS, BOOKMARKED_ITEM);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_BOOKMARKED_ITEMS + "/#", BOOKMARKED_ITEM_ID);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_BOOKMARKED_KIDS, BOOKMARKED_KID);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_BOOKMARKED_KIDS + "/#", BOOKMARKED_KID_ID);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_BOOKMARKED_PARTS, BOOKMARKED_PART);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_BOOKMARKED_PARTS + "/#", BOOKMARKED_PART_ID);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_READ_HISTORY, READ_HISTORY);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_READ_HISTORY + "/#", READ_HISTORY_ID);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_USERS, USER);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_USERS + "/#", USER_ID);

        return uriMatcher;
    }

    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
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

        Cursor retCursor;
        String tableName;

        switch (sUriMatcher.match(uri)) {
            case BOOKMARKED_ITEM:
                tableName = DatabaseContract.BookmarkedItemEntry.TABLE_NAME;
                break;
            case BOOKMARKED_KID:
                tableName = DatabaseContract.BookmarkedKidEntry.TABLE_NAME;
                break;
            case BOOKMARKED_PART:
                tableName = DatabaseContract.BookmarkedPartEntry.TABLE_NAME;
                break;
            case READ_HISTORY:
                tableName = DatabaseContract.ReadHistoryEntry.TABLE_NAME;
                break;
            case USER:
                tableName = DatabaseContract.UserEntry.TABLE_NAME;
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

        Uri returnUri;

        String tableName;
        Uri contentUri;

        switch (sUriMatcher.match(uri)) {
            case BOOKMARKED_ITEM:
                tableName = DatabaseContract.BookmarkedItemEntry.TABLE_NAME;
                contentUri = DatabaseContract.BookmarkedItemEntry.CONTENT_URI;
                break;
            case BOOKMARKED_KID:
                tableName = DatabaseContract.BookmarkedKidEntry.TABLE_NAME;
                contentUri = DatabaseContract.BookmarkedKidEntry.CONTENT_URI;
                break;
            case BOOKMARKED_PART:
                tableName = DatabaseContract.BookmarkedPartEntry.TABLE_NAME;
                contentUri = DatabaseContract.BookmarkedPartEntry.CONTENT_URI;
                break;
            case READ_HISTORY:
                tableName = DatabaseContract.ReadHistoryEntry.TABLE_NAME;
                contentUri = DatabaseContract.ReadHistoryEntry.CONTENT_URI;
                break;
            case USER:
                tableName = DatabaseContract.UserEntry.TABLE_NAME;
                contentUri = DatabaseContract.UserEntry.CONTENT_URI;
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

        String tableName;

        switch (sUriMatcher.match(uri)) {
            case BOOKMARKED_ITEM:
                tableName = DatabaseContract.BookmarkedItemEntry.TABLE_NAME;
                break;
            case BOOKMARKED_KID:
                tableName = DatabaseContract.BookmarkedKidEntry.TABLE_NAME;
                break;
            case BOOKMARKED_PART:
                tableName = DatabaseContract.BookmarkedPartEntry.TABLE_NAME;
                break;
            case READ_HISTORY:
                tableName = DatabaseContract.ReadHistoryEntry.TABLE_NAME;
                break;
            case USER:
                tableName = DatabaseContract.UserEntry.TABLE_NAME;
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

        int deleted;

        long id;
        String tableName;

        switch (sUriMatcher.match(uri)) {
            case BOOKMARKED_ITEM:
                tableName = DatabaseContract.BookmarkedItemEntry.TABLE_NAME;
                deleted = db.delete(tableName, selection, selectionArgs);
                break;
            case BOOKMARKED_ITEM_ID:
                id = ContentUris.parseId(uri);
                tableName = DatabaseContract.BookmarkedItemEntry.TABLE_NAME;
                deleted = db.delete(tableName, BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
                break;
            case BOOKMARKED_KID:
                tableName = DatabaseContract.BookmarkedKidEntry.TABLE_NAME;
                deleted = db.delete(tableName, selection, selectionArgs);
                break;
            case BOOKMARKED_KID_ID:
                id = ContentUris.parseId(uri);
                tableName = DatabaseContract.BookmarkedKidEntry.TABLE_NAME;
                deleted = db.delete(tableName, BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
                break;
            case BOOKMARKED_PART:
                tableName = DatabaseContract.BookmarkedPartEntry.TABLE_NAME;
                deleted = db.delete(tableName, selection, selectionArgs);
                break;
            case BOOKMARKED_PART_ID:
                id = ContentUris.parseId(uri);
                tableName = DatabaseContract.BookmarkedPartEntry.TABLE_NAME;
                deleted = db.delete(tableName, BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
                break;
            case READ_HISTORY:
                tableName = DatabaseContract.ReadHistoryEntry.TABLE_NAME;
                deleted = db.delete(tableName, selection, selectionArgs);
                break;
            case READ_HISTORY_ID:
                id = ContentUris.parseId(uri);
                tableName = DatabaseContract.ReadHistoryEntry.TABLE_NAME;
                deleted = db.delete(tableName, BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
                break;
            case USER:
                tableName = DatabaseContract.UserEntry.TABLE_NAME;
                deleted = db.delete(tableName, selection, selectionArgs);
                break;
            case USER_ID:
                id = ContentUris.parseId(uri);
                tableName = DatabaseContract.UserEntry.TABLE_NAME;
                deleted = db.delete(tableName, BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

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

        int updated;

        long id = ContentUris.parseId(uri);
        String tableName;

        switch (sUriMatcher.match(uri)) {
            case BOOKMARKED_ITEM_ID:
                tableName = DatabaseContract.BookmarkedItemEntry.TABLE_NAME;
                break;
            case BOOKMARKED_KID_ID:
                tableName = DatabaseContract.BookmarkedKidEntry.TABLE_NAME;
                break;
            case BOOKMARKED_PART_ID:
                tableName = DatabaseContract.BookmarkedPartEntry.TABLE_NAME;
                break;
            case READ_HISTORY_ID:
                tableName = DatabaseContract.ReadHistoryEntry.TABLE_NAME;
                break;
            case USER_ID:
                tableName = DatabaseContract.UserEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        updated = db.update(tableName, values, BaseColumns._ID + "=?", new String[]{String.valueOf(id)});

        if (updated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case BOOKMARKED_ITEM:
                return TYPE_DIRECTORY + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_BOOKMARKED_ITEMS;
            case BOOKMARKED_ITEM_ID:
                return TYPE_ITEM + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_BOOKMARKED_ITEMS;
            case BOOKMARKED_KID:
                return TYPE_DIRECTORY + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_BOOKMARKED_KIDS;
            case BOOKMARKED_KID_ID:
                return TYPE_ITEM + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_BOOKMARKED_KIDS;
            case BOOKMARKED_PART:
                return TYPE_DIRECTORY + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_BOOKMARKED_PARTS;
            case BOOKMARKED_PART_ID:
                return TYPE_ITEM + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_BOOKMARKED_PARTS;
            case READ_HISTORY:
                return TYPE_DIRECTORY + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_READ_HISTORY;
            case READ_HISTORY_ID:
                return TYPE_ITEM + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_READ_HISTORY;
            case USER:
                return TYPE_DIRECTORY + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_USERS;
            case USER_ID:
                return TYPE_ITEM + "/" +
                        DatabaseContract.AUTHORITY + "/" +
                        DatabaseContract.PATH_USERS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
