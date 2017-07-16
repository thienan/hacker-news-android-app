package com.marcelje.hackernews.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.marcelje.hackernews.R;

public class ToolbarActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(R.layout.toolbar_item, layoutResID);
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
