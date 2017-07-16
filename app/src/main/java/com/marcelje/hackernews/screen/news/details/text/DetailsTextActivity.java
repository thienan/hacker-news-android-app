package com.marcelje.hackernews.screen.news.details.text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.marcelje.hackernews.R;

public class DetailsTextActivity extends AppCompatActivity {

    private static final String EXTRA_TEXT = "com.marcelje.hackernews.screen.news.details.text.extra.TEXT";

    public static void startActivity(Activity activity, String text) {
        Intent intent = new Intent(activity, DetailsTextActivity.class);
        intent.putExtra(EXTRA_TEXT, text);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TEXT)) {
            String text = intent.getStringExtra(EXTRA_TEXT);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_text_container, DetailsTextActivityFragment.newInstance(text))
                    .commit();
        }
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
}
