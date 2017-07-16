package com.marcelje.hackernews.screen.news.details.text;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;

public class DetailsTextActivity extends ToolbarActivity {

    private static final String EXTRA_TEXT = "com.marcelje.hackernews.screen.news.details.text.extra.TEXT";

    private String mText;

    public static void startActivity(ToolbarActivity activity, String text) {
        Intent intent = new Intent(activity, DetailsTextActivity.class);

        Bundle extras = createExtras(text);
        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_text);
        setDisplayHomeAsUpEnabled(true);
        extractExtras();
        attachFragment();
    }

    private static Bundle createExtras(String text) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_TEXT, text);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TEXT)) {
            mText = intent.getStringExtra(EXTRA_TEXT);
        }
    }

    private void attachFragment() {
        Fragment fragment = DetailsTextActivityFragment.newInstance(mText);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.details_text_container, fragment)
                .commit();
    }
}
