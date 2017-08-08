package com.marcelljee.hackernews.handlers;

import com.marcelljee.hackernews.screen.news.item.text.DetailsTextActivity;

import com.marcelljee.hackernews.activity.ToolbarActivity;

public class ItemTextDetailsClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemTextDetailsClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(String text) {
        DetailsTextActivity.startActivity(mActivity, text);
    }
}