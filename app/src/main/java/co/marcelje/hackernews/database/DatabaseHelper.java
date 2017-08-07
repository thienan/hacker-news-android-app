package co.marcelje.hackernews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HackerNews.db";
    private static final int VERSION = 1;

    private static final String CREATE_BOOKMARKED_ITEM_TABLE = "CREATE TABLE "
            + DatabaseContract.BookmarkedItemEntry.TABLE_NAME + " (" +
            DatabaseContract.BookmarkedItemEntry._ID + " INTEGER PRIMARY KEY, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_DELETED + " INTEGER, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_TYPE + " TEXT, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_BY + " TEXT, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_TIME + " INTEGER, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_TEXT + " TEXT, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_DEAD + " INTEGER, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_PARENT + " INTEGER, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_POLL + " REAL, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_URL + " TEXT, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_SCORE + " INTEGER, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_TITLE + " TEXT, " +
            DatabaseContract.BookmarkedItemEntry.COLUMN_DESCENDANTS + " INTEGER " + ");";

    private static final String CREATE_BOOKMARKED_KID_TABLE = "CREATE TABLE "
            + DatabaseContract.BookmarkedKidEntry.TABLE_NAME + " (" +
            DatabaseContract.BookmarkedKidEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.BookmarkedKidEntry.COLUMN_ITEM_ID + " INTEGER, " +
            DatabaseContract.BookmarkedKidEntry.COLUMN_KID_ID + " INTEGER " + ");";

    private static final String CREATE_BOOKMARKED_PART_TABLE = "CREATE TABLE "
            + DatabaseContract.BookmarkedPartEntry.TABLE_NAME + " (" +
            DatabaseContract.BookmarkedPartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.BookmarkedPartEntry.COLUMN_ITEM_ID + " INTEGER, " +
            DatabaseContract.BookmarkedPartEntry.COLUMN_PART_ID + " INTEGER " + ");";

    private static final String CREATE_READ_HISTORY_TABLE = "CREATE TABLE "
            + DatabaseContract.ReadHistoryEntry.TABLE_NAME + " (" +
            DatabaseContract.ReadHistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.ReadHistoryEntry.COLUMN_ITEM_ID + " INTEGER, " +
            DatabaseContract.ReadHistoryEntry.COLUMN_READ_DATE + " INTEGER " + ");";

    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            + DatabaseContract.UserEntry.TABLE_NAME + " (" +
            DatabaseContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.UserEntry.COLUMN_USER_ID + " TEXT, " +
            DatabaseContract.UserEntry.COLUMN_DELAY + " INTEGER, " +
            DatabaseContract.UserEntry.COLUMN_CREATED + " INTEGER, " +
            DatabaseContract.UserEntry.COLUMN_KARMA + " INTEGER, " +
            DatabaseContract.UserEntry.COLUMN_ABOUT + " TEXT " + ");";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKMARKED_ITEM_TABLE);
        db.execSQL(CREATE_BOOKMARKED_KID_TABLE);
        db.execSQL(CREATE_BOOKMARKED_PART_TABLE);
        db.execSQL(CREATE_READ_HISTORY_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
