package com.marcelljee.hackernews.screen.news.item.text;

import android.content.Intent;
import android.os.Bundle;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.activity.FragmentActivity;

public class DetailsTextActivity extends FragmentActivity {

    private static final String EXTRA_TEXT = "com.marcelljee.hackernews.screen.news.item.text.extra.TEXT";

    public static void startActivity(ToolbarActivity activity, String text) {
        Intent intent = new Intent(activity, DetailsTextActivity.class);

        Bundle extras = createExtras(text);
        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String text = intent.getStringExtra(EXTRA_TEXT);

        if (savedInstanceState == null) {
            setFragment(DetailsTextFragment.newInstance(text));
        }
    }

    private static Bundle createExtras(String text) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_TEXT, text);

        return extras;
    }
}
