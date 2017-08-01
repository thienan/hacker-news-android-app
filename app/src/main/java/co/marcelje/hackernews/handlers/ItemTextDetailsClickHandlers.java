package co.marcelje.hackernews.handlers;

import co.marcelje.hackernews.screen.news.item.text.DetailsTextActivity;

import co.marcelje.hackernews.activity.ToolbarActivity;

public class ItemTextDetailsClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemTextDetailsClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(String text) {
        DetailsTextActivity.startActivity(mActivity, text);
    }
}