package com.marcelje.hackernews.handlers;

import android.view.View;
import android.widget.ImageButton;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.model.Item;

public class ItemBookmarkClickHandlers {

    private final ToolbarActivity mActivity;

    public ItemBookmarkClickHandlers(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void onClick(View view, Item data) {
        if (view instanceof ImageButton) {
            ImageButton bookmarkButton = (ImageButton) view;

            if (HackerNewsDao.isItemAvailable(mActivity, data.getId())) {
                HackerNewsDao.deleteItem(mActivity, data.getId());
                SnackbarFactory.createUnbookmarkedSuccessSnackBar(view).show();
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_border);
            } else {
                HackerNewsDao.insertItem(mActivity, data);
                SnackbarFactory.createBookmarkedSuccessSnackBar(view).show();
                bookmarkButton.setImageResource(R.drawable.ic_bookmark);
            }
        }
    }
}