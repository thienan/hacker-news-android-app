package com.marcelje.hackernews.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.marcelje.hackernews.database.HackerNewsContract;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@Parcel(Parcel.Serialization.BEAN)
public class Item {
    private long id;
    private boolean deleted;
    private String type;
    private String by;
    private long time;
    private String text;
    private boolean dead;
    private long parent;
    private long poll;
    private List<Long> kids = new ArrayList<>();
    private String url;
    private int score;
    private String title;
    private List<Long> parts = new ArrayList<>();
    private int descendants;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isNotDeleted() {
        return !deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isNotDead() {
        return !dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public long getPoll() {
        return poll;
    }

    public void setPoll(long poll) {
        this.poll = poll;
    }

    public List<Long> getKids() {
        return kids;
    }

    public void setKids(List<Long> kids) {
        this.kids = kids;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getParts() {
        return parts;
    }

    public void setParts(List<Long> parts) {
        this.parts = parts;
    }

    public int getDescendants() {
        return descendants;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }

    public static class Factory {
        public static ContentValues toValues(Item item) {
            ContentValues values = new ContentValues();
            if (item == null) return values;

            values.put(HackerNewsContract.BookmarkedItemEntry._ID, item.getId());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_DELETED, item.isDeleted());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_TYPE, item.getType());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_BY, item.getBy());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_TIME, item.getTime());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_TEXT, item.getText());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_DEAD, item.isDead());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_PARENT, item.getParent());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_POLL, item.getPoll());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_URL, item.getUrl());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_SCORE, item.getScore());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_TITLE, item.getTitle());
            values.put(HackerNewsContract.BookmarkedItemEntry.COLUMN_DESCENDANTS, item.getDescendants());

            return values;
        }

        public static List<Item> fromCursor(Cursor cursor) {
            List<Item> items = new ArrayList<>();
            if (cursor == null) return items;

            while (cursor.moveToNext()) {
                Item item = new Item();
                item.setId(cursor.getLong(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry._ID)));
                item.setDeleted(cursor.getInt(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_DELETED)) == 1);
                item.setType(cursor.getString(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_TYPE)));
                item.setBy(cursor.getString(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_BY)));
                item.setTime(cursor.getLong(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_TIME)));
                item.setText(cursor.getString(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_TEXT)));
                item.setDead(cursor.getInt(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_DEAD)) == 1);
                item.setParent(cursor.getLong(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_PARENT)));
                item.setPoll(cursor.getLong(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_POLL)));
                item.setUrl(cursor.getString(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_URL)));
                item.setScore(cursor.getInt(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_SCORE)));
                item.setTitle(cursor.getString(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_TITLE)));
                item.setDescendants(cursor.getInt(cursor.getColumnIndex(HackerNewsContract.BookmarkedItemEntry.COLUMN_DESCENDANTS)));

                items.add(item);
            }

            return items;
        }
    }
}