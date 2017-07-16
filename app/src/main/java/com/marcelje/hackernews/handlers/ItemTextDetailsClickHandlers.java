package com.marcelje.hackernews.handlers;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.screen.news.details.text.DetailsTextActivity;

public class ItemTextDetailsClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemTextDetailsClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(String text) {
        DetailsTextActivity.startActivity(mActivity, text);
    }
}