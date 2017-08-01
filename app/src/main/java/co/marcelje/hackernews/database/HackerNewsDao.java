package co.marcelje.hackernews.database;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import co.marcelje.hackernews.model.Item;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public final class HackerNewsDao {

    private HackerNewsDao() {
    }

    public static boolean isItemAvailable(Context context, long itemId) {
        if (context == null || itemId <= 0) return false;

        Cursor cur = getItem(context, itemId);
        boolean isBookmarked = cur.getCount() > 0;
        cur.close();

        return isBookmarked;
    }

    private static Cursor getItem(Context context, long itemId) {
        return context.getContentResolver().query(HackerNewsContract.BookmarkedItemEntry.CONTENT_URI,
                null, BaseColumns._ID + "=?", new String[]{String.valueOf(itemId)}, null);
    }

    public static List<Item> getItems(Context context) {
        if (context == null) return Collections.emptyList();

        List<Item> items;

        Cursor cursor = context.getContentResolver().query(HackerNewsContract.BookmarkedItemEntry.CONTENT_URI, null, null, null, null);

        items = Item.Factory.fromCursor(cursor);

        for (Item item : items) {
            cursor = context.getContentResolver().query(
                    HackerNewsContract.BookmarkedKidEntry.CONTENT_URI, null,
                    HackerNewsContract.BookmarkedKidEntry.COLUMN_ITEM_ID + "=?",
                    new String[]{String.valueOf(item.getId())}, null);

            item.setKids(Item.Factory.kidsFromCursor(cursor));

            cursor = context.getContentResolver().query(
                    HackerNewsContract.BookmarkedPartEntry.CONTENT_URI, null,
                    HackerNewsContract.BookmarkedPartEntry.COLUMN_ITEM_ID + "=?",
                    new String[]{String.valueOf(item.getId())}, null);

            item.setParts(Item.Factory.partsFromCursor(cursor));
        }

        if (cursor != null) {
            cursor.close();
        }

        return items;
    }

    public static int deleteItem(Context context, long itemId) {
        Uri deletedUri = ContentUris.withAppendedId(HackerNewsContract.BookmarkedItemEntry.CONTENT_URI, itemId);

        context.getContentResolver().delete(
                HackerNewsContract.BookmarkedKidEntry.CONTENT_URI,
                HackerNewsContract.BookmarkedKidEntry.COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)});

        context.getContentResolver().delete(
                HackerNewsContract.BookmarkedPartEntry.CONTENT_URI,
                HackerNewsContract.BookmarkedPartEntry.COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)});

        return context.getContentResolver().delete(deletedUri, null, null);
    }

    public static Uri insertItem(Context context, Item item) {
        context.getContentResolver()
                .bulkInsert(HackerNewsContract.BookmarkedKidEntry.CONTENT_URI, Item.Factory.kidsToValues(item));
        context.getContentResolver()
                .bulkInsert(HackerNewsContract.BookmarkedPartEntry.CONTENT_URI, Item.Factory.partsToValues(item));

        return context.getContentResolver()
                .insert(HackerNewsContract.BookmarkedItemEntry.CONTENT_URI, Item.Factory.toValues(item));
    }
}
