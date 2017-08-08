package com.marcelljee.hackernews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.marcelljee.hackernews.R;

public class ScoreView extends AppCompatTextView {

    private static final int TEXT_SIZE = 14;

    private boolean mRead = false;

    public ScoreView(Context context) {
        super(context);
        initView();
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initView();
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        setGravity(Gravity.CENTER);
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        setTextSize(TEXT_SIZE);
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
