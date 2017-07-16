package com.marcelje.hackernews.handlers;

import android.app.Activity;

import com.marcelje.hackernews.screen.user.UserActivity;

public class ItemUserClickHandlers {

    private final Activity mActivity;

    public ItemUserClickHandlers(Activity activity) {
        mActivity = activity;
    }

    public void onClick(String userId) {
        UserActivity.startActivity(mActivity, userId);
    }
}