package com.marcelje.hackernews.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.marcelje.hackernews.R;

public class ToolbarActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    public static ToolbarActivity getActivity(Activity activity) {
        if (activity instanceof ToolbarActivity) {
            return (ToolbarActivity) activity;
        }

        return null;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(R.layout.toolbar_item, layoutResID);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.no_change, R.anim.slide_right);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_right);
    }

    void setContentView(@LayoutRes int toolbarResID, @LayoutRes int layoutResID) {
        View toolbar = getLayoutInflater().inflate(toolbarResID, null, false);
        FrameLayout container = toolbar.findViewById(R.id.layout_container);
        getLayoutInflater().inflate(layoutResID, container, true);

        mToolbar = toolbar.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        super.setContentView(toolbar);
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @SuppressWarnings("SameParameterValue")
    protected void setDisplayShowTitleEnabled(boolean showTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }
}