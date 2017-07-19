package com.marcelje.hackernews.screen.news.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.utils.BrowserUtils;
import com.marcelje.hackernews.utils.HackerNewsUtils;
import com.marcelje.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

public class NewsDetailsActivity extends ToolbarActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.details.extra.ITEM";

    private NewsDetailsActivityFragment mFragment;
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
        setContentView(R.layout.activity_base);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_bookmark);

        if (HackerNewsDao.isItemAvailable(this, mItem.getId())) {
            menuItem.setTitle(R.string.menu_item_unbookmark);
        } else {
            menuItem.setTitle(R.string.menu_item_bookmark);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_refresh:
                mFragment.refreshComments();

                return true;
            case R.id.action_share:
                MenuUtils.openShareHackerNewsLinkChooser(this, mItem);

                return true;
            case R.id.action_bookmark:
                if (HackerNewsDao.isItemAvailable(this, mItem.getId())) {
                    HackerNewsDao.deleteItem(this, mItem.getId());
                    SnackbarFactory.createUnbookmarkedSuccessSnackBar(getToolbar()).show();
                    menuItem.setTitle(R.string.menu_item_bookmark);
                } else {
                    HackerNewsDao.insertItem(this, mItem);
                    SnackbarFactory.createBookmarkedSuccessSnackBar(getToolbar()).show();
                    menuItem.setTitle(R.string.menu_item_unbookmark);
                }

                return true;
            case R.id.action_open_page:
                BrowserUtils.openTab(this, HackerNewsUtils.geItemUrl(mItem.getId()));

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
        mFragment = NewsDetailsActivityFragment.newInstance(mItem);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragment)
                .commit();
    }
}
