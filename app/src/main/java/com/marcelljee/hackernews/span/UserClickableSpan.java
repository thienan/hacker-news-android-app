package com.marcelljee.hackernews.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;

public class UserClickableSpan extends ClickableSpan {

    private final ToolbarActivity mActivity;
    private final String mUserId;

    public UserClickableSpan(ToolbarActivity activity, String userId) {
        mActivity = activity;
        mUserId = userId;
    }

    @Override
    public void onClick(View view) {
        UserActivity.startActivity(mActivity, mUserId);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //override do nothing
    }
}