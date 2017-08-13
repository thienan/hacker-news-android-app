package com.marcelljee.hackernews.viewmodel;

import android.support.customtabs.CustomTabsSession;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelljee.hackernews.handlers.ItemTextClickHandlers;
import com.marcelljee.hackernews.handlers.ItemUserClickHandlers;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.ItemUtils;

public class ItemNewsViewModel {

    private final ToolbarActivity mActivity;
    private final ItemUserClickHandlers mUserClickHandlers;
    private final ItemTextClickHandlers mTextClickHandlers;
    private final ItemBookmarkClickHandlers mBookmarkClickHandlers;

    private final boolean mReadIndicator;

    public ItemNewsViewModel(ToolbarActivity activity, boolean readIndicator, CustomTabsSession session) {
        mActivity = activity;
        mUserClickHandlers = new ItemUserClickHandlers(activity);
        mTextClickHandlers = new ItemTextClickHandlers(activity, session);
        mBookmarkClickHandlers = new ItemBookmarkClickHandlers(activity);

        mReadIndicator = readIndicator;
    }

    public int isItemViewVisible(Item item) {
        return item.isDeleted() || item.isDead() ? View.GONE : View.VISIBLE;
    }

    public int isDeletedViewVisible(Item item) {
        return item.isDeleted() ? View.VISIBLE : View.GONE;
    }

    public int isDeadViewVisible(Item item) {
        return item.isDead() && !item.isDeleted() ? View.VISIBLE : View.GONE;
    }

    public int isCommentViewVisible(Item item) {
        return item.getDescendants() > 0 ? View.VISIBLE : View.GONE;
    }

    public boolean isItemRead(Item item) {
        return mReadIndicator && item.isRead();
    }

    public String getScoreText(Item item) {
        return String.valueOf(item.getScore());
    }

    public CharSequence getRelativeDate(Item item) {
        return ItemUtils.getRelativeDate(mActivity, item);
    }

    public SpannableStringBuilder getTitle(Item item) {
        return ItemUtils.getTitle(mActivity, item);
    }

    public void userClick(String userId) {
        mUserClickHandlers.onClick(userId);
    }

    public void textClick(Item item) {
        mTextClickHandlers.onClick(item);
    }

    public void bookmarkClick(Item item) {
        mBookmarkClickHandlers.onClick(item);
    }
}
