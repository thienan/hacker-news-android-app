package com.marcelljee.hackernews.viewmodel;

import android.text.SpannableStringBuilder;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.handlers.ItemUserClickHandlers;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.ItemUtils;

public class ItemCommentViewModel {

    private final ToolbarActivity mActivity;
    private final ItemUserClickHandlers mUserClickHandlers;

    public ItemCommentViewModel(ToolbarActivity activity) {
        mActivity = activity;
        mUserClickHandlers = new ItemUserClickHandlers(activity);
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

    public SpannableStringBuilder getStyledText(Item item) {
        return ItemUtils.fromHtml(mActivity, item.getText());
    }

    public void userClick(String userId) {
        mUserClickHandlers.onClick(userId);
    }
}
