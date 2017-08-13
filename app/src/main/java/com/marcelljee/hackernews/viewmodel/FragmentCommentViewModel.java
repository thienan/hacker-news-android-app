package com.marcelljee.hackernews.viewmodel;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.utils.ItemUtils;

public class FragmentCommentViewModel {

    private ToolbarActivity mActivity;
    private String mItemParentName;
    private String mItemPosterName;

    public FragmentCommentViewModel(ToolbarActivity activity, String itemParentName, String itemPosterName) {
        mActivity = activity;
        mItemParentName = itemParentName;
        mItemPosterName = itemPosterName;
    }

    public int isCommentInfoViewVisible() {
        return TextUtils.isEmpty(mItemParentName) && TextUtils.isEmpty(mItemPosterName) ? View.GONE : View.VISIBLE;
    }

    public SpannableStringBuilder getCommentInfo() {
        return ItemUtils.getCommentInfo(mActivity, mItemParentName, mItemPosterName);
    }
}
