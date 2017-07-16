package com.marcelje.hackernews.screen.news.details.text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;

public class DetailsTextActivity extends ToolbarActivity {

    private static final String EXTRA_TEXT = "com.marcelje.hackernews.screen.news.details.text.extra.TEXT";

    private String mText;

    public static void startActivity(Activity activity, String text) {
        Intent intent = new Intent(activity, DetailsTextActivity.class);

        Bundle extras = createExtras(text);
        intent.putExtras(extras);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_text);
        setDisplayHomeAsUpEnabled(true);
        extractExtras();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.details_text_container, DetailsTextActivityFragment.newInstance(mText))
                .commit();
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
}
