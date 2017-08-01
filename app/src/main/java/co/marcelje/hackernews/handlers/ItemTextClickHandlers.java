package co.marcelje.hackernews.handlers;

import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.model.Item;
import co.marcelje.hackernews.screen.news.item.BaseItemActivity;
import co.marcelje.hackernews.chrome.CustomTabsBrowser;

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