package com.marcelje.hackernews.handlers;

import android.text.TextUtils;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.details.NewsDetailsActivity;
import com.marcelje.hackernews.utils.BrowserUtils;

public class ItemTextClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemTextClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(Item data) {
        if (TextUtils.isEmpty(data.getUrl())) {
            NewsDetailsActivity.startActivity(mActivity, data);
        } else {
            BrowserUtils.openTab(mActivity, data.getUrl());
        }
    }
}