package com.marcelljee.hackernews.databinding.viewmodel;

import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.event.ItemUpdateEvent;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;
import com.marcelljee.hackernews.utils.SettingsUtils;

import org.greenrobot.eventbus.EventBus;

public class ItemViewModel {

    private final ToolbarActivity mActivity;
    private final CustomTabsSession mCustomTabsSession;
    private final boolean mReadIndicatorEnabled;

    public ItemViewModel(ToolbarActivity activity) {
        this(activity, false, null);
    }

    public ItemViewModel(ToolbarActivity activity, boolean readIndicator, CustomTabsSession customTabsSession) {
        mActivity = activity;
        mCustomTabsSession = customTabsSession;
        mReadIndicatorEnabled = readIndicator;
    }

    public boolean getReadIndicatorEnabled() {
        return mReadIndicatorEnabled;
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

        EventBus.getDefault().post(new ItemUpdateEvent(item));
    }

    public void userClick(String userId) {
        if (!UserActivity.class.getName().equals(mActivity.getClass().getName())) {
            UserActivity.startActivity(mActivity, userId);
        }
    }
}
