package com.marcelljee.hackernews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.marcelljee.hackernews.R;

public class BookmarkView extends AppCompatImageButton {

    private boolean mBookmarked = false;

    public BookmarkView(Context context) {
        super(context);
    }

    public BookmarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public BookmarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BookmarkView,
                0, 0);

        try {
            mBookmarked = a.getBoolean(R.styleable.BookmarkView_bookmarked, false);
        } finally {
            a.recycle();
        }
    }

    public void setBookmarked(boolean bookmarked) {
        mBookmarked = bookmarked;
        updateImage();
    }

    private void updateImage() {
        if (mBookmarked) {
            setImageResource(R.drawable.ic_bookmarked);
        } else {
            setImageResource(R.drawable.ic_unbookmarked);
        }
    }

}
