package co.marcelje.hackernews.handlers;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.screen.user.UserActivity;

public class ItemUserClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemUserClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(String userId) {
        UserActivity.startActivity(mActivity, userId);
    }
}