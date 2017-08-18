package com.marcelljee.hackernews.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class RevealViewOnClickSpan extends ClickableSpan {

    private final View mView;

    public RevealViewOnClickSpan(View view) {
        mView = view;
    }

    @Override
    public void onClick(View widget) {
        if (mView.getVisibility() == View.VISIBLE) {
            mView.setVisibility(View.GONE);
        } else {
            mView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(true);
    }
}