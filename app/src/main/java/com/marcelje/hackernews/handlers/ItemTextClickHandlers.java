package com.marcelje.hackernews.handlers;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.details.NewsDetailsActivity;
import com.marcelje.hackernews.utils.BrowserUtils;

public class ItemTextClickHandlers {

    private final Activity mActivity;

    public ItemTextClickHandlers(Activity activity) {
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