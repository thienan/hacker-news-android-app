package co.marcelje.hackernews.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String AUTHORITY = "co.marcelje.hackernews.database.AUTHORITY";
    @SuppressWarnings("WeakerAccess")
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_BOOKMARKED_ITEMS = "bookmarked_items";
    public static final String PATH_BOOKMARKED_KIDS = "bookmarked_kids";
    public static final String PATH_BOOKMARKED_PARTS = "bookmarked_parts";
    public static final String PATH_ITEM_HISTORY = "item_history";
    public static final String PATH_ITEM_READ = "item_read";

    public static final class BookmarkedItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARKED_ITEMS).build();

        public static final String TABLE_NAME = PATH_BOOKMARKED_ITEMS;

        public static final String COLUMN_DELETED = "deleted";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_BY = "by";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DEAD = "dead";
        public static final String COLUMN_PARENT = "parent";
        public static final String COLUMN_POLL = "poll";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCENDANTS = "descendants";
    }

    public static final class BookmarkedKidEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARKED_KIDS).build();

        public static final String TABLE_NAME = PATH_BOOKMARKED_KIDS;

        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_KID_ID = "kid_id";
    }

    public static final class BookmarkedPartEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARKED_PARTS).build();

        public static final String TABLE_NAME = PATH_BOOKMARKED_PARTS;

        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_PART_ID = "part_id";
    }

    public static final class ItemHistoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM_HISTORY).build();

        public static final String TABLE_NAME = PATH_ITEM_HISTORY;

        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_READ_DATE = "read_date";
    }

    public static final class ItemReadEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM_READ).build();

        public static final String TABLE_NAME = PATH_ITEM_READ;
    }
}
