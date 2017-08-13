package com.marcelljee.hackernews.handlers;

import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.BaseItemActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;

public class ItemTextClickHandlers {

    private final ToolbarActivity mActivity;
    private final CustomTabsSession mSession;

    public ItemTextClickHandlers(ToolbarActivity activity) {
        this(activity, null);
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