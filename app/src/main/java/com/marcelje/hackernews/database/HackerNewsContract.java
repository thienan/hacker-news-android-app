package com.marcelje.hackernews.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class HackerNewsContract {

    public static final String AUTHORITY = "com.marcelje.hackernews.database.AUTHORITY";
    @SuppressWarnings("WeakerAccess")
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_BOOKMARKED_ITEMS = "bookmarked_items";
    public static final String PATH_USERS = "users";

    public static final class BookmarkedItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKMARKED_ITEMS).build();

        public static final String TABLE_NAME = "bookmarked_items";

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

    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_DELAY = "delay";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_KARMA = "karma";
        public static final String COLUMN_ABOUT = "about";

    }
}
