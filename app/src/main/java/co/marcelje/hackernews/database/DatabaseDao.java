package co.marcelje.hackernews.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import co.marcelje.hackernews.model.Item;
import co.marcelje.hackernews.utils.DatabaseUtils;
import co.marcelje.hackernews.utils.SettingsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"WeakerAccess"})
public final class DatabaseDao {

    private DatabaseDao() {
    }

    public static boolean isItemBookmarked(Context context, long itemId) {
        if (context == null || itemId <= 0) return false;

        Cursor cur = getBookmarkedItem(context, itemId);
        boolean isBookmarked = cur.getCount() > 0;
        cur.close();

        return isBookmarked;
    }

    private static Cursor getBookmarkedItem(Context context, long itemId) {
        return context.getContentResolver().query(DatabaseContract.BookmarkedItemEntry.CONTENT_URI,
                null, BaseColumns._ID + "=?", new String[]{String.valueOf(itemId)}, null);
    }

    public static List<Item> getBookmarkedItems(Context context) {
        if (context == null) return Collections.emptyList();

        List<Item> items;

        Cursor cursor = context.getContentResolver()
                .query(DatabaseContract.BookmarkedItemEntry.CONTENT_URI,
                        null, null, null, null);

        items = Item.Factory.fromCursor(cursor);

        for (Item item : items) {
            cursor = context.getContentResolver().query(
                    DatabaseContract.BookmarkedKidEntry.CONTENT_URI, null,
                    DatabaseContract.BookmarkedKidEntry.COLUMN_ITEM_ID + "=?",
                    new String[]{String.valueOf(item.getId())}, null);

            item.setKids(Item.Factory.kidsFromCursor(cursor));

            cursor = context.getContentResolver().query(
                    DatabaseContract.BookmarkedPartEntry.CONTENT_URI, null,
                    DatabaseContract.BookmarkedPartEntry.COLUMN_ITEM_ID + "=?",
                    new String[]{String.valueOf(item.getId())}, null);

            item.setParts(Item.Factory.partsFromCursor(cursor));
        }

        if (cursor != null) {
            cursor.close();
        }

        return items;
    }

    public static void deleteBookmarkedItem(Context context, long itemId) {
        DatabaseUpdaterService.startActionDelete(context,
                DatabaseContract.BookmarkedKidEntry.CONTENT_URI,
                DatabaseContract.BookmarkedKidEntry.COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)});

        DatabaseUpdaterService.startActionDelete(context,
                DatabaseContract.BookmarkedPartEntry.CONTENT_URI,
                DatabaseContract.BookmarkedPartEntry.COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)});

        DatabaseUpdaterService.startActionDelete(context, ContentUris.withAppendedId(
                DatabaseContract.BookmarkedItemEntry.CONTENT_URI, itemId),
                null, null);
    }

    public static void insertBookmarkedItem(Context context, Item item) {
        DatabaseUpdaterService.startActionBulkInsert(context,
                DatabaseContract.BookmarkedKidEntry.CONTENT_URI, Item.Factory.kidsToValues(item));

        DatabaseUpdaterService.startActionBulkInsert(context,
                DatabaseContract.BookmarkedPartEntry.CONTENT_URI, Item.Factory.partsToValues(item));

        DatabaseUpdaterService.startActionInsert(context,
                DatabaseContract.BookmarkedItemEntry.CONTENT_URI, Item.Factory.toValues(item));
    }

    public static List<Long> getHistoryItems(Context context) {
        if (context == null) return Collections.emptyList();

        Cursor cursor = context.getContentResolver()
                .query(DatabaseContract.ItemHistoryEntry.CONTENT_URI,
                        null, null, null, BaseColumns._ID + " DESC");

        List<Long> mItemIds = new ArrayList<>();
        if (cursor == null) return mItemIds;

        while (cursor.moveToNext()) {
            mItemIds.add(DatabaseUtils.getLong(cursor, DatabaseContract.ItemHistoryEntry.COLUMN_ITEM_ID));
        }

        cursor.close();

        return mItemIds;
    }

    public static void insertHistoryItem(Context context, Item item) {
        if (!SettingsUtils.historyEnabled(context)) return;

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ItemHistoryEntry.COLUMN_ITEM_ID, item.getId());
        values.put(DatabaseContract.ItemHistoryEntry.COLUMN_READ_DATE, System.currentTimeMillis());

        DatabaseUpdaterService.startActionInsert(context,
                DatabaseContract.ItemHistoryEntry.CONTENT_URI, values);
    }

    public static void deleteAllHistoryItem(Context context) {
        DatabaseUpdaterService.startActionDelete(context,
                DatabaseContract.ItemHistoryEntry.CONTENT_URI, null, null);
    }

    public static boolean isItemRead(Context context, long itemId) {
        if (context == null || itemId <= 0) return false;

        Cursor cur = getReadItem(context, itemId);
        boolean isRead = cur.getCount() > 0;
        cur.close();

        return isRead;
    }

    private static Cursor getReadItem(Context context, long itemId) {
        return context.getContentResolver().query(DatabaseContract.ItemReadEntry.CONTENT_URI,
                null, BaseColumns._ID + "=?", new String[]{String.valueOf(itemId)}, null);
    }

    public static void insertReadItem(Context context, Item item) {
        if (isItemRead(context, item.getId())) return;

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ItemReadEntry._ID, item.getId());

        DatabaseUpdaterService.startActionInsert(context,
                DatabaseContract.ItemReadEntry.CONTENT_URI, values);
    }

    public static void deleteReadItem(Context context, long itemId) {
        DatabaseUpdaterService.startActionDelete(context,
                DatabaseContract.ItemReadEntry.CONTENT_URI,
                DatabaseContract.ItemReadEntry._ID + "=?",
                new String[]{String.valueOf(itemId)});
    }
}
