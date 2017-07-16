package com.marcelje.hackernews.handlers;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.screen.user.UserActivity;

public class ItemUserClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemUserClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(String userId) {
        UserActivity.startActivity(mActivity, userId);
    }
}