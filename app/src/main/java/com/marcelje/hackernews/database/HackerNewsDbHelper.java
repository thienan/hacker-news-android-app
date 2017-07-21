package com.marcelje.hackernews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class HackerNewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HackerNews.db";
    private static final int VERSION = 1;

    private static final String CREATE_BOOKMARKED_ITEM_TABLE = "CREATE TABLE "
            + HackerNewsContract.BookmarkedItemEntry.TABLE_NAME + " (" +
            HackerNewsContract.BookmarkedItemEntry._ID + " INTEGER PRIMARY KEY, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_DELETED + " INTEGER, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_TYPE + " TEXT, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_BY + " TEXT, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_TIME + " INTEGER, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_TEXT + " TEXT, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_DEAD + " INTEGER, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_PARENT + " INTEGER, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_POLL + " REAL, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_URL + " TEXT, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_SCORE + " INTEGER, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_TITLE + " TEXT, " +
            HackerNewsContract.BookmarkedItemEntry.COLUMN_DESCENDANTS + " INTEGER " + ");";

    private static final String CREATE_BOOKMARKED_KID_TABLE = "CREATE TABLE "
            + HackerNewsContract.BookmarkedKidEntry.TABLE_NAME + " (" +
            HackerNewsContract.BookmarkedKidEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HackerNewsContract.BookmarkedKidEntry.COLUMN_ITEM_ID + " INTEGER, " +
            HackerNewsContract.BookmarkedKidEntry.COLUMN_KID_ID + " INTEGER " + ");";

    private static final String CREATE_BOOKMARKED_PART_TABLE = "CREATE TABLE "
            + HackerNewsContract.BookmarkedPartEntry.TABLE_NAME + " (" +
            HackerNewsContract.BookmarkedPartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HackerNewsContract.BookmarkedPartEntry.COLUMN_ITEM_ID + " INTEGER, " +
            HackerNewsContract.BookmarkedPartEntry.COLUMN_PART_ID + " INTEGER " + ");";

    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            + HackerNewsContract.UserEntry.TABLE_NAME + " (" +
            HackerNewsContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HackerNewsContract.UserEntry.COLUMN_USER_ID + " TEXT, " +
            HackerNewsContract.UserEntry.COLUMN_DELAY + " INTEGER, " +
            HackerNewsContract.UserEntry.COLUMN_CREATED + " INTEGER, " +
            HackerNewsContract.UserEntry.COLUMN_KARMA + " INTEGER, " +
            HackerNewsContract.UserEntry.COLUMN_ABOUT + " TEXT " + ");";



    public HackerNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKMARKED_ITEM_TABLE);
        db.execSQL(CREATE_BOOKMARKED_KID_TABLE);
        db.execSQL(CREATE_BOOKMARKED_PART_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
