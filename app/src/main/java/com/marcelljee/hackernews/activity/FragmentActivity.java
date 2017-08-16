package com.marcelljee.hackernews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.marcelljee.hackernews.R;

public class FragmentActivity<T extends Fragment> extends ToolbarActivity {

    private static final String TAG_FRAGMENT = "com.marcelljee.hackernews.activity.tag.FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void setFragment(T fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, TAG_FRAGMENT)
                .commit();
    }

    protected T getFragment() {
        return (T) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
