package com.marcelljee.hackernews.viewmodel;

import android.support.customtabs.CustomTabsSession;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.event.ItemBookmarkEvent;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;
import com.marcelljee.hackernews.utils.ItemUtils;
import com.marcelljee.hackernews.utils.SettingsUtils;

import org.greenrobot.eventbus.EventBus;

public class ItemNewsViewModel {

    private final ToolbarActivity mActivity;
    private final CustomTabsSession mCustomTabsSession;

    private final boolean mReadIndicator;

    public ItemNewsViewModel(ToolbarActivity activity, boolean readIndicator, CustomTabsSession customTabsSession) {
        mActivity = activity;
        mCustomTabsSession = customTabsSession;

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

    public boolean getReadIndicator() {
        return mReadIndicator;
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
        if (!UserActivity.class.getName().equals(mActivity.getClass().getName())) {
            UserActivity.startActivity(mActivity, userId);
        }
    }

    public void textClick(Item item) {
        if (TextUtils.isEmpty(item.getUrl())) {
            ItemActivity.startActivity(mActivity, item);
        } else {
            CustomTabsBrowser.openTab(mActivity, mCustomTabsSession, item.getUrl());
        }

        DatabaseDao.insertHistoryItem(mActivity, item);
        DatabaseDao.insertReadIndicatorItem(mActivity, item.getId());

        if (SettingsUtils.readIndicatorEnabled(mActivity)) {
            item.setRead(true);
        }
    }

    public void bookmarkClick(Item item) {
        if (DatabaseDao.isItemBookmarked(mActivity, item.getId())) {
            DatabaseDao.deleteBookmarkedItem(mActivity, item.getId());
            item.setBookmarked(false);
        } else {
            DatabaseDao.insertBookmarkedItem(mActivity, item);
            item.setBookmarked(true);
        }

        if (ItemActivity.StoryActivity.class.getName().equals(mActivity.getClass().getName())) {
            EventBus.getDefault().post(new ItemBookmarkEvent.StoryActivityEvent());
        } else if (UserActivity.class.getName().equals(mActivity.getClass().getName())) {
            EventBus.getDefault().post(new ItemBookmarkEvent.UserActivityEvent());
        }
    }
}
