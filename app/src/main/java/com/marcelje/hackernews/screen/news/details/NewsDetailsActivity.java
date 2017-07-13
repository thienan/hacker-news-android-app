package com.marcelje.hackernews.screen.news.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;

import org.parceler.Parcels;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.details.extra.ITEM";

    public static void startActivity(Context context, Item item) {
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(EXTRA_ITEM, Parcels.wrap(item));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM)) {
            Item item = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.comment_container, NewsDetailsActivityFragment.newInstance(item))
                    .commit();
        }
    }
}
