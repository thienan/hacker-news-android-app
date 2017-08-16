package com.marcelljee.hackernews.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.marcelljee.hackernews.BR;
import com.marcelljee.hackernews.database.DatabaseContract;
import com.marcelljee.hackernews.utils.DatabaseUtils;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@Parcel(Parcel.Serialization.BEAN)
public class Item extends BaseObservable {

    public static final long NO_ID = -1;

    private long id = NO_ID;
    private boolean deleted = false;
    private String type = "";
    private String by = "";
    private long time = 0;
    private String text = "";
    private boolean dead = false;
    private long parent = -1;
    private long poll = -1;
    private List<Long> kids = new ArrayList<>();
    private String url = "";
    private int score = 0;
    private String title = "";
    private List<Long> parts = new ArrayList<>();
    private int descendants = 0;

    private boolean bookmarked = false;
    private boolean read = false;

    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public boolean isDeleted() {
        return deleted;
    }

    @Bindable
    public boolean isNotDeleted() {
        return !deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
        notifyPropertyChanged(BR.deleted);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
        notifyPropertyChanged(BR.by);
    }

    @Bindable
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public boolean isDead() {
        return dead;
    }

    @Bindable
    public boolean isNotDead() {
        return !dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
        notifyPropertyChanged(BR.dead);
    }

    @Bindable
    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
        notifyPropertyChanged(BR.parent);
    }

    @Bindable
    public long getPoll() {
        return poll;
    }

    public void setPoll(long poll) {
        this.poll = poll;
        notifyPropertyChanged(BR.poll);
    }

    @Bindable
    public List<Long> getKids() {
        return kids;
    }

    public void setKids(List<Long> kids) {
        this.kids = kids;
        notifyPropertyChanged(BR.kids);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        notifyPropertyChanged(BR.score);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public List<Long> getParts() {
        return parts;
    }

    public void setParts(List<Long> parts) {
        this.parts = parts;
        notifyPropertyChanged(BR.parts);
    }

    @Bindable
    public int getDescendants() {
        return descendants;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
        notifyPropertyChanged(BR.descendants);
    }

    @Bindable
    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
        notifyPropertyChanged(BR.bookmarked);
    }

    @Bindable
    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
        notifyPropertyChanged(BR.read);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (deleted != item.deleted) return false;
        if (time != item.time) return false;
        if (dead != item.dead) return false;
        if (parent != item.parent) return false;
        if (poll != item.poll) return false;
        if (score != item.score) return false;
        if (descendants != item.descendants) return false;
        if (bookmarked != item.bookmarked) return false;
        if (read != item.read) return false;
        if (type != null ? !type.equals(item.type) : item.type != null) return false;
        if (!by.equals(item.by)) return false;
        if (text != null ? !text.equals(item.text) : item.text != null) return false;
        if (kids != null ? !kids.equals(item.kids) : item.kids != null) return false;
        if (url != null ? !url.equals(item.url) : item.url != null) return false;
        if (title != null ? !title.equals(item.title) : item.title != null) return false;
        return parts != null ? parts.equals(item.parts) : item.parts == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + by.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (dead ? 1 : 0);
        result = 31 * result + (int) (parent ^ (parent >>> 32));
        result = 31 * result + (int) (poll ^ (poll >>> 32));
        result = 31 * result + (kids != null ? kids.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + score;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (parts != null ? parts.hashCode() : 0);
        result = 31 * result + descendants;
        result = 31 * result + (bookmarked ? 1 : 0);
        result = 31 * result + (read ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", type='" + type + '\'' +
                ", by='" + by + '\'' +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", dead=" + dead +
                ", parent=" + parent +
                ", poll=" + poll +
                ", kids=" + kids +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", title='" + title + '\'' +
                ", parts=" + parts +
                ", descendants=" + descendants +
                ", bookmarked=" + bookmarked +
                ", read=" + read +
                '}';
    }

    public static class Factory {
        public static ContentValues toValues(Item item) {
            ContentValues values = new ContentValues();
            if (item == null) return values;

            values.put(DatabaseContract.BookmarkedItemEntry._ID, item.getId());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_DELETED, item.isDeleted());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_TYPE, item.getType());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_BY, item.getBy());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_TIME, item.getTime());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_TEXT, item.getText());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_DEAD, item.isDead());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_PARENT, item.getParent());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_POLL, item.getPoll());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_URL, item.getUrl());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_SCORE, item.getScore());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_TITLE, item.getTitle());
            values.put(DatabaseContract.BookmarkedItemEntry.COLUMN_DESCENDANTS, item.getDescendants());

            return values;
        }

        public static List<Item> fromCursor(Cursor cursor) {
            List<Item> items = new ArrayList<>();
            if (cursor == null) return items;

            while (cursor.moveToNext()) {
                Item item = new Item();
                item.setId(DatabaseUtils.getLong(cursor,
                        DatabaseContract.BookmarkedItemEntry._ID));
                item.setDeleted(DatabaseUtils.getBoolean(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_DELETED));
                item.setType(DatabaseUtils.getString(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_TYPE));
                item.setBy(DatabaseUtils.getString(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_BY));
                item.setTime(DatabaseUtils.getLong(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_TIME));
                item.setText(DatabaseUtils.getString(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_TEXT));
                item.setDead(DatabaseUtils.getBoolean(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_DEAD));
                item.setParent(DatabaseUtils.getLong(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_PARENT));
                item.setPoll(DatabaseUtils.getLong(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_POLL));
                item.setUrl(DatabaseUtils.getString(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_URL));
                item.setScore(DatabaseUtils.getInt(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_SCORE));
                item.setTitle(DatabaseUtils.getString(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_TITLE));
                item.setDescendants(DatabaseUtils.getInt(cursor,
                        DatabaseContract.BookmarkedItemEntry.COLUMN_DESCENDANTS));

                items.add(item);
            }

            return items;
        }

        public static ContentValues[] kidsToValues(Item item) {
            List<ContentValues> valuesList = new ArrayList<>();

            for (long kidId : item.getKids()) {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.BookmarkedKidEntry.COLUMN_ITEM_ID, item.getId());
                values.put(DatabaseContract.BookmarkedKidEntry.COLUMN_KID_ID, kidId);

                valuesList.add(values);
            }

            return valuesList.toArray(new ContentValues[valuesList.size()]);
        }

        public static List<Long> kidsFromCursor(Cursor cursor) {
            List<Long> kidList = new ArrayList<>();
            if (cursor == null) return kidList;

            while (cursor.moveToNext()) {
                long kidId = DatabaseUtils.getLong(cursor,
                        DatabaseContract.BookmarkedKidEntry.COLUMN_KID_ID);
                kidList.add(kidId);
            }

            return kidList;
        }

        public static ContentValues[] partsToValues(Item item) {
            List<ContentValues> valuesList = new ArrayList<>();

            for (long partId : item.getParts()) {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.BookmarkedPartEntry.COLUMN_ITEM_ID, item.getId());
                values.put(DatabaseContract.BookmarkedPartEntry.COLUMN_PART_ID, partId);

                valuesList.add(values);
            }

            return valuesList.toArray(new ContentValues[valuesList.size()]);
        }

        public static List<Long> partsFromCursor(Cursor cursor) {
            List<Long> partList = new ArrayList<>();
            if (cursor == null) return partList;

            while (cursor.moveToNext()) {
                long partId = DatabaseUtils.getLong(cursor,
                        DatabaseContract.BookmarkedPartEntry.COLUMN_PART_ID);
                partList.add(partId);
            }

            return partList;
        }
    }
}