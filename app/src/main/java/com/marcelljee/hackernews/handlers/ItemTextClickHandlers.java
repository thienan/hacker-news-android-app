package com.marcelljee.hackernews.handlers;

import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.utils.SettingsUtils;

public class ItemTextClickHandlers {

    private final ToolbarActivity mActivity;
    private final CustomTabsSession mSession;

    public ItemTextClickHandlers(ToolbarActivity activity, CustomTabsSession session) {
        mActivity = activity;
        mSession = session;
    }

    public void onClick(Item item) {
        if (TextUtils.isEmpty(item.getUrl())) {
            ItemActivity.startActivity(mActivity, item);
        } else {
            CustomTabsBrowser.openTab(mActivity, mSession, item.getUrl());
        }

        DatabaseDao.insertHistoryItem(mActivity, item);
        DatabaseDao.insertReadIndicatorItem(mActivity, item.getId());

        if (SettingsUtils.readIndicatorEnabled(mActivity)) {
            item.setRead(true);
        }
    }
}