package co.marcelje.hackernews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import co.marcelje.hackernews.R;

public class ScoreView extends AppCompatTextView {

    private static final int TEXT_SIZE = 14;

    public ScoreView(Context context) {
        super(context);
        initView();
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setBackgroundResource(R.drawable.ic_circle);
        setGravity(Gravity.CENTER);
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        setTextSize(TEXT_SIZE);
    }
}
