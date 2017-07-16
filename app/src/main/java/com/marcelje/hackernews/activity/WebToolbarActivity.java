package com.marcelje.hackernews.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;

import com.marcelje.hackernews.R;

public class WebToolbarActivity extends ToolbarActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(R.layout.toolbar_web, layoutResID);
    }

    @SuppressWarnings("SameParameterValue")
    protected void setDisplayHomeAsCloseEnabled(boolean showHomeAsClose) {
        Drawable close = ContextCompat.getDrawable(this, R.drawable.ic_close);
        close.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsClose);
            getSupportActionBar().setHomeAsUpIndicator(close);
        }
    }
}
