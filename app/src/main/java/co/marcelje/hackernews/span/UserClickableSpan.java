package co.marcelje.hackernews.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.screen.user.UserActivity;

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