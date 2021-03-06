package com.marcelljee.hackernews.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.marcelljee.hackernews.span.CustomTabUrlSpan;

public class LinkifiedTextView extends AppCompatTextView {
    private CustomTabUrlSpan mPressedSpan;

    public LinkifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TextView widget = this;
        Object text = widget.getText();
        if (text instanceof Spannable) {
            Spannable buffer = (Spannable) text;

            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP) {
                if (mPressedSpan != null) {
                    mPressedSpan.onClick(widget);
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(buffer);
                    return true;
                }
            } else if (action == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(widget, buffer, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(buffer, buffer.getSpanStart(mPressedSpan),
                            buffer.getSpanEnd(mPressedSpan));
                    return true;
                }
            } else if (action == MotionEvent.ACTION_MOVE) {
                CustomTabUrlSpan touchedSpan = getPressedSpan(widget, buffer, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(buffer);
                    return true;
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                }
                Selection.removeSelection(buffer);
                return super.onTouchEvent(event);
            }
        }

        return false;
    }

    private CustomTabUrlSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        CustomTabUrlSpan[] link = spannable.getSpans(off, off, CustomTabUrlSpan.class);
        CustomTabUrlSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }
}