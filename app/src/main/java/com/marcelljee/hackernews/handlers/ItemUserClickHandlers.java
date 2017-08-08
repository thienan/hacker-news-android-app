package com.marcelljee.hackernews.handlers;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;

public class ItemUserClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemUserClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(String userId) {
        UserActivity.startActivity(mActivity, userId);
    }
}