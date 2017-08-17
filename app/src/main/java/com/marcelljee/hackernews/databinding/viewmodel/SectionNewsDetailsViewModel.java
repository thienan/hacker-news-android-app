package com.marcelljee.hackernews.databinding.viewmodel;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.screen.news.item.text.DetailsTextActivity;

public class SectionNewsDetailsViewModel {

    private final ToolbarActivity mActivity;

    public SectionNewsDetailsViewModel(ToolbarActivity activity) {
        mActivity = activity;
    }

    public void textDetailsClick(String text) {
        if (!DetailsTextActivity.class.getName().equals(mActivity.getClass().getName())) {
            DetailsTextActivity.startActivity(mActivity, text);
        }
    }
}
