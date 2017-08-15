package com.marcelljee.hackernews.viewmodel;

import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.user.UserActivity;
import com.marcelljee.hackernews.utils.ItemUtils;

public class ItemViewModel {

    private final ToolbarActivity mActivity;

    public ItemViewModel(ToolbarActivity activity) {
        mActivity = activity;
    }

    public int isItemViewVisible(Item item) {
        return item.isDeleted() || item.isDead() ? View.GONE : View.VISIBLE;
    }

    public int isDeletedViewVisible(Item item) {
        return item.isDeleted() ? View.VISIBLE : View.GONE;
    }

    public int isDeadViewVisible(Item item) {
        return item.isDead() && !item.isDeleted() ? View.VISIBLE : View.GONE;
    }

    public CharSequence getRelativeDate(Item item) {
        return ItemUtils.getRelativeDate(mActivity, item);
    }

    public void userClick(String userId) {
        if (!UserActivity.class.getName().equals(mActivity.getClass().getName())) {
            UserActivity.startActivity(mActivity, userId);
        }
    }

    public ToolbarActivity getActivity() {
        return mActivity;
    }
}
