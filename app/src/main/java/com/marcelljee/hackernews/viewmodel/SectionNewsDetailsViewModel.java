package com.marcelljee.hackernews.viewmodel;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.screen.news.item.text.DetailsTextActivity;
import com.marcelljee.hackernews.utils.ItemUtils;

public class SectionNewsDetailsViewModel {

    private final ToolbarActivity mActivity;

    public SectionNewsDetailsViewModel(ToolbarActivity activity) {
        mActivity = activity;
    }

    public int isViewVisible(String text) {
        return TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE;
    }

    public SpannableStringBuilder getStyledText(String text) {
        return ItemUtils.fromHtml(mActivity, text);
    }

    public void textDetailsClick(String text) {
        DetailsTextActivity.startActivity(mActivity, text);
    }
}
