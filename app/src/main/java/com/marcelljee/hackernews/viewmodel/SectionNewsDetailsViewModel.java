package com.marcelljee.hackernews.viewmodel;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.handlers.ItemTextDetailsClickHandlers;
import com.marcelljee.hackernews.utils.ItemUtils;

public class SectionNewsDetailsViewModel {

    private ToolbarActivity mActivity;
    private ItemTextDetailsClickHandlers mTextDetailsClickHandlers;

    public SectionNewsDetailsViewModel(ToolbarActivity activity) {
        mActivity = activity;
        mTextDetailsClickHandlers = new ItemTextDetailsClickHandlers(activity);
    }

    public int isViewVisible(String text) {
        return TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE;
    }

    public SpannableStringBuilder getStyledText(String text) {
        return ItemUtils.fromHtml(mActivity, text);
    }

    public void textDetailsClick(String text) {
        mTextDetailsClickHandlers.onClick(text);
    }
}
