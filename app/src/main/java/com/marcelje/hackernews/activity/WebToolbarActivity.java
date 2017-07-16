package com.marcelje.hackernews.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

import com.marcelje.hackernews.R;

public class WebToolbarActivity extends ToolbarActivity {

    protected static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(R.layout.toolbar_web, layoutResID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_change, R.anim.slide_down);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
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
