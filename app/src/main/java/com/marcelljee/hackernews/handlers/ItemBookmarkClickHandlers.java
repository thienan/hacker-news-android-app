package com.marcelljee.hackernews.handlers;

import org.greenrobot.eventbus.EventBus;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.event.ItemBookmarkEvent;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;

public class ItemBookmarkClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemBookmarkClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(Item item) {
        if (DatabaseDao.isItemBookmarked(mActivity, item.getId())) {
            DatabaseDao.deleteBookmarkedItem(mActivity, item.getId());
            item.setBookmarked(false);
        } else {
            DatabaseDao.insertBookmarkedItem(mActivity, item);
            item.setBookmarked(true);
        }

        if (ItemActivity.StoryActivity.class.getName().equals(mActivity.getClass().getName())) {
            EventBus.getDefault().post(new ItemBookmarkEvent.StoryActivityEvent());
        } else if (UserActivity.class.getName().equals(mActivity.getClass().getName())) {
            EventBus.getDefault().post(new ItemBookmarkEvent.UserActivityEvent());
        }
    }
}