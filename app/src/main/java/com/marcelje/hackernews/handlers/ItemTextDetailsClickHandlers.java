package com.marcelje.hackernews.handlers;

import android.app.Activity;

import com.marcelje.hackernews.screen.news.details.text.DetailsTextActivity;

public class ItemTextDetailsClickHandlers {

    private final Activity mActivity;

    public ItemTextDetailsClickHandlers(Activity activity) {
        mActivity = activity;
    }

    public void onClick(String text) {
        DetailsTextActivity.startActivity(mActivity, text);
    }
}