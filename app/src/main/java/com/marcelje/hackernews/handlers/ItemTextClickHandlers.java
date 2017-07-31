package com.marcelje.hackernews.handlers;

import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.BaseItemActivity;
import com.marcelje.hackernews.chrome.CustomTabsBrowser;

public class ItemTextClickHandlers {

    private final ToolbarActivity mActivity;
    private final CustomTabsSession mSession;

    public ItemTextClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
        mSession = null;
    }

    public ItemTextClickHandlers(ToolbarActivity activity, CustomTabsSession session) {
        mActivity = activity;
        mSession = session;
    }

    public void onClick(Item data) {
        if (TextUtils.isEmpty(data.getUrl())) {
            BaseItemActivity.startActivity(mActivity, data);
        } else {
            CustomTabsBrowser.openTab(mActivity, mSession, data.getUrl());
        }
    }
}