package com.marcelljee.hackernews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.marcelljee.hackernews.R;

public class CheckedScoreView extends AppCompatImageView {

    private boolean mRead = false;

    public CheckedScoreView(Context context) {
        super(context);
    }

    public CheckedScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initView();
    }

    public CheckedScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ScoreView,
                0, 0);

        try {
            mRead = a.getBoolean(R.styleable.ScoreView_read, false);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        setBackgroundResource(R.drawable.ic_circle);
        setImageResource(R.drawable.ic_check);

        int padding = getResources().getDimensionPixelSize(R.dimen.view_margin);
        setPadding(padding, padding, padding, padding);
        updateBackgroundColor();
    }

    public void setRead(boolean read) {
        mRead = read;
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        if (mRead) {
            ((GradientDrawable) getBackground())
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
        } else {
            ((GradientDrawable) getBackground())
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        }
    }
}
