package com.marcelljee.hackernews.viewmodel;

import android.text.SpannableStringBuilder;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.ItemUtils;

public class ItemCommentViewModel extends ItemViewModel {

    public ItemCommentViewModel(ToolbarActivity activity) {
        super(activity);
    }

    public SpannableStringBuilder getStyledText(Item item) {
        return ItemUtils.fromHtml(getActivity(), item.getText());
    }
}
