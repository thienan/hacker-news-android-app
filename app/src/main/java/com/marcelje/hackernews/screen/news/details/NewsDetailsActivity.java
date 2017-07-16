package com.marcelje.hackernews.screen.news.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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

    public static void startActivity(Activity activity, Item item) {
        Intent intent = new Intent(activity, NewsDetailsActivity.class);

        Bundle extras = createExtras(item);
        intent.putExtras(extras);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        setDisplayHomeAsUpEnabled(true);
        extractExtras();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.news_details_container, NewsDetailsActivityFragment.newInstance(mItem))
                .commit();
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.no_change, R.anim.slide_right);
                return true;
            case R.id.action_share:
                MenuUtils.openShareHackerNewsLinkChooser(this, mItem);
                return true;
            default:
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_right);
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
}
