package com.marcelje.hackernews.screen.news.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

public class NewsDetailsActivity extends ToolbarActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.details.extra.ITEM";

    private Item mItem;

    public static void startActivity(ToolbarActivity activity, Item item) {
        Intent intent = new Intent(activity, NewsDetailsActivity.class);

        Bundle extras = createExtras(item);
        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        setDisplayHomeAsUpEnabled(true);
        extractExtras();
        attachFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_share:
                MenuUtils.openShareHackerNewsLinkChooser(this, mItem);
                return true;
            default:
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private static Bundle createExtras(Item item) {
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_ITEM, Parcels.wrap(item));

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ITEM)) {
            mItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));
        }
    }

    private void attachFragment() {
        Fragment fragment = NewsDetailsActivityFragment.newInstance(mItem);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.news_details_container, fragment)
                .commit();
    }
}
