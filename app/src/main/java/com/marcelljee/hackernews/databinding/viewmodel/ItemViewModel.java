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

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel {

    private final ToolbarActivity mActivity;
    private final List<Item> mItems;
    private final CustomTabsSession mCustomTabsSession;
    private final boolean mReadIndicatorEnabled;

    public ItemViewModel(ToolbarActivity activity, List<Item> items) {
        this(activity, items, false, null);
    }

    public ItemViewModel(ToolbarActivity activity, List<Item> items, boolean readIndicator, CustomTabsSession customTabsSession) {
        mActivity = activity;
        mItems = new ArrayList<>(items);
        mCustomTabsSession = customTabsSession;
        mReadIndicatorEnabled = readIndicator;
    }

    public void swapItems(List<Item> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    public void updateItem(Item item) {
        mItems.set(mItems.indexOf(item), item);
    }

    public boolean getReadIndicatorEnabled() {
        return mReadIndicatorEnabled;
    }

    public void textClick(int itemPosition) {
        Item item = mItems.get(itemPosition);

        if (TextUtils.isEmpty(item.getUrl())) {
            ItemActivity.startActivity(mActivity, mItems, itemPosition);
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
