package com.marcelljee.hackernews.viewmodel;

import android.text.SpannableStringBuilder;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.User;
import com.marcelljee.hackernews.utils.ItemUtils;

public class FragmentUserViewModel {

    private final ToolbarActivity mActivity;

    public FragmentUserViewModel(ToolbarActivity activity) {
       mActivity = activity;
    }

    public CharSequence getRelativeDate(User user) {
        return ItemUtils.getRelativeDate(mActivity, user);
    }

    public SpannableStringBuilder getAbout(User user) {
        if (user == null) return new SpannableStringBuilder();
        return ItemUtils.fromHtml(mActivity, user.getAbout());
    }
}
