package com.marcelljee.hackernews.databinding.viewmodel;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.screen.news.item.text.DetailsTextActivity;

public class SectionNewsDetailsViewModel {

    private final ToolbarActivity mActivity;

    public SectionNewsDetailsViewModel(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void textDetailsClick(String text) {
        DetailsTextActivity.startActivity(mActivity, text);
    }
}
