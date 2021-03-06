package com.marcelljee.hackernews.span;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;

@SuppressLint("ParcelCreator")
public class CustomTabUrlSpan extends URLSpan {

    private final ToolbarActivity mActivity;

    private boolean mIsPressed;
    private final int mPressedBackgroundColor;
    private final int mNormalTextColor;
    private final int mPressedTextColor;

    public CustomTabUrlSpan(ToolbarActivity activity, String url) {
        super(url);
        mActivity = activity;

        mNormalTextColor = ContextCompat.getColor(activity, R.color.colorAccent);
        mPressedTextColor = ContextCompat.getColor(activity, R.color.textColorSecondary);
        mPressedBackgroundColor = ContextCompat.getColor(activity, R.color.colorBackground);
    }

    @Override
    public void onClick(View view) {
        CustomTabsBrowser.openTab(mActivity, getURL());
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = mIsPressed ? mPressedBackgroundColor : 0xffeeeeee;
        ds.setTypeface(Typeface.create(ds.getTypeface(), Typeface.BOLD));
        ds.setUnderlineText(false);
    }

    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }
}