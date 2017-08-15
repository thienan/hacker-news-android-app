package com.marcelljee.hackernews.viewmodel;

import android.text.SpannableStringBuilder;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.user.UserActivity;
import com.marcelljee.hackernews.utils.ItemUtils;

public class ItemCommentViewModel extends ItemViewModel {

    public ItemCommentViewModel(ToolbarActivity activity) {
        super(activity);
    }

    public SpannableStringBuilder getStyledText(Item item) {
        return ItemUtils.fromHtml(getActivity(), item.getText());
    }
}
