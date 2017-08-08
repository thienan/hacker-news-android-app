package com.marcelljee.hackernews.handlers;

import android.view.View;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.event.ItemBookmarkEvent;
import com.marcelljee.hackernews.factory.SnackbarFactory;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.StoryActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;

public class ItemBookmarkClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemBookmarkClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(View view, Item data) {
        if (view instanceof ImageButton) {
            ImageButton bookmarkButton = (ImageButton) view;

            if (DatabaseDao.isItemBookmarked(mActivity, data.getId())) {
                DatabaseDao.deleteBookmarkedItem(mActivity, data.getId());
                SnackbarFactory.createUnbookmarkedSuccessSnackBar(view).show();
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_border);
            } else {
                DatabaseDao.insertBookmarkedItem(mActivity, data);
                SnackbarFactory.createBookmarkedSuccessSnackBar(view).show();
                bookmarkButton.setImageResource(R.drawable.ic_bookmark);
            }

            if (StoryActivity.class.getName().equals(mActivity.getClass().getName())) {
                EventBus.getDefault().post(new ItemBookmarkEvent.StoryActivityEvent());
            } else if (UserActivity.class.getName().equals(mActivity.getClass().getName())) {
                EventBus.getDefault().post(new ItemBookmarkEvent.UserActivityEvent());
            }
        }
    }
}