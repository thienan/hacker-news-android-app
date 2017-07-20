package com.marcelje.hackernews.screen.news.item;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.FragmentActivity;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.model.Item;

import org.parceler.Parcels;

public class ItemActivity extends FragmentActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.item.extra.ITEM";
    private static final String EXTRA_PARENT = "com.marcelje.hackernews.screen.news.item.extra.PARENT";
    private static final String EXTRA_POSTER = "com.marcelje.hackernews.screen.news.item.extra.POSTER";

    private ItemFragment mFragment;
    private Item mItem;
    private String mParent;
    private String mPoster;

    public static void startActivity(ToolbarActivity activity, Item item) {
        startActivity(activity, item, null, null);
    }

    public static void startActivity(ToolbarActivity activity, Item item, String parent, String poster) {
        Intent intent = new Intent(activity, ItemActivity.class);

        Bundle extras = createExtras(item, parent, poster);
        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        extractExtras();

        setTitle(mItem.getType());

        mFragment = ItemFragment.newInstance(mItem, mParent, mPoster);
        setFragment(mFragment);
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
                mFragment.refreshPage();
                return true;
            case R.id.action_share:
                mFragment.share();
                return true;
            case R.id.action_bookmark:
                mFragment.bookmark(menuItem);
                return true;
            case R.id.action_open_page:
                mFragment.openPage();
                return true;
            default:
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private static Bundle createExtras(Item item, String parent, String poster) {
        Bundle extras = new Bundle();
        if (item != null) extras.putParcelable(EXTRA_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) extras.putString(EXTRA_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) extras.putString(EXTRA_POSTER, poster);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ITEM)) {
            mItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));
        }

        if (intent.hasExtra(EXTRA_PARENT)) {
            mParent = intent.getStringExtra(EXTRA_PARENT);
        }

        if (intent.hasExtra(EXTRA_POSTER)) {
            mPoster = intent.getStringExtra(EXTRA_POSTER);
        }
    }

}
