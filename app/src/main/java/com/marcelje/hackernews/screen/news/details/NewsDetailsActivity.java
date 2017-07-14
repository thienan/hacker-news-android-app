package com.marcelje.hackernews.screen.news.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.details.extra.ITEM";

    private Item mItem;

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
            mItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.news_details_container, NewsDetailsActivityFragment.newInstance(mItem))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_share:
                MenuUtils.openShareChooser(this, mItem);
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
