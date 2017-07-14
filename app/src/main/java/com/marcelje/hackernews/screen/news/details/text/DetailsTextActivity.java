package com.marcelje.hackernews.screen.news.details.text;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.marcelje.hackernews.R;

public class DetailsTextActivity extends AppCompatActivity {

    private static final String EXTRA_TEXT = "com.marcelje.hackernews.screen.news.details.text.extra.TEXT";

    public static void startActivity(Context context, String text) {
        Intent intent = new Intent(context, DetailsTextActivity.class);
        intent.putExtra(EXTRA_TEXT, text);
        context.startActivity(intent);
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

}
