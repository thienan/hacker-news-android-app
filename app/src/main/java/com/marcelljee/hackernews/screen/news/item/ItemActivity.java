package com.marcelljee.hackernews.screen.news.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.FragmentActivity;
import com.marcelljee.hackernews.loader.HackerNewsResponse;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.screen.news.NewsActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.utils.HackerNewsUtils;
import com.marcelljee.hackernews.utils.ItemUtils;
import com.marcelljee.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;

public class ItemActivity extends FragmentActivity<ItemFragment>
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String EXTRA_ROOT_CALLER_ACTIVITY = "com.marcelljee.hackernews.screen.news.item.extra.ROOT_CALLER_ACTIVITY";
    public static final String EXTRA_ITEM = "com.marcelljee.hackernews.screen.news.item.extra.ITEM";
    private static final String EXTRA_ITEM_PARENT_NAME = "com.marcelljee.hackernews.screen.news.item.extra.ITEM_PARENT_NAME";
    private static final String EXTRA_ITEM_POSTER_NAME = "com.marcelljee.hackernews.screen.news.item.extra.ITEM_POSTER_NAME";

    private static final String STATE_PARENT_ID = "com.marcelljee.hackernews.screen.news.item.state.PARENT_ID";
    private static final String STATE_PARENT_ITEM = "com.marcelljee.hackernews.screen.news.item.state.PARENT_ITEM";

    public static final String ITEM_TYPE_COMMENT = "comment";
    public static final String ITEM_TYPE_STORY = "story";
    public static final String ITEM_TYPE_POLL = "poll";
    public static final String ITEM_TYPE_JOB = "job";

    private static final int LOADER_PARENT_ITEM = 300;

    private String mRootCallerActivity;
    private Item mItem;
    private String mItemParentName;
    private String mItemPosterName;

    private long mParentId;
    private Item mParentItem;

    public static void startActivity(ToolbarActivity activity, Item item) {
        startActivity(activity, item, null, null);
    }

    public static void startActivity(ToolbarActivity activity, Item item,
                                     String itemParentName, String itemPosterName) {
        switch (item.getType()) {
            case ITEM_TYPE_COMMENT:
                startActivity(activity,
                        CommentActivity.createIntent(activity), item, itemParentName, itemPosterName);
                break;
            case ITEM_TYPE_STORY:
            case ITEM_TYPE_POLL:
            case ITEM_TYPE_JOB:
            default:
                startActivity(activity,
                        StoryActivity.createIntent(activity), item, itemParentName, itemPosterName);
                break;
        }
    }

    private static void startActivity(ToolbarActivity activity, Intent intent, Item item,
                                      String itemParentName, String itemPosterName) {
        Bundle extras = createExtras(activity, item, itemParentName, itemPosterName);
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        extractExtras();

        setTitle(ItemUtils.getTypeAsTitle(mItem));

        if (savedInstanceState == null) {
            setFragment(ItemFragment.newInstance(mItem, mItemParentName, mItemPosterName));
            loadParentItem();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mParentId = savedInstanceState.getLong(STATE_PARENT_ID);
        mParentItem = Parcels.unwrap(savedInstanceState.getParcelable(STATE_PARENT_ITEM));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_PARENT_ID, mParentId);
        outState.putParcelable(STATE_PARENT_ITEM, Parcels.wrap(mParentItem));
        super.onSaveInstanceState(outState);
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
                if (!NewsActivity.class.getName().equals(mRootCallerActivity)
                        && mItem.getParent() > 0) {
                    if (mParentItem != null) {
                        ItemActivity.startActivity(this, mParentItem);
                    } else {
                        loadParentItem();
                    }

                    return true;
                }

                return super.onOptionsItemSelected(menuItem);
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_open_page:
                openPage();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_PARENT_ITEM:
                return new ItemListLoader(this, Collections.singletonList(mParentId));
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader,
                               HackerNewsResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_PARENT_ITEM:
                    mParentItem = response.getData().get(0);
                    break;
                default:
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    private void loadParentItem() {
        mParentId = mItem.getParent();

        if (mParentId > 0) {
            getSupportLoaderManager().restartLoader(LOADER_PARENT_ITEM, null, this);
        }
    }

    private static Bundle createExtras(Activity activity, Item item,
                                       String itemParentName, String itemPosterName) {
        Bundle extras = new Bundle();
        if (activity != null) {
            String rootCallerActivity;
            if (activity.getIntent() != null
                    && activity.getIntent().hasExtra(EXTRA_ROOT_CALLER_ACTIVITY)) {
                rootCallerActivity = activity.getIntent().getStringExtra(EXTRA_ROOT_CALLER_ACTIVITY);
            } else {
                rootCallerActivity = activity.getClass().getName();
            }
            extras.putString(EXTRA_ROOT_CALLER_ACTIVITY, rootCallerActivity);
        }
        if (item != null) extras.putParcelable(EXTRA_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(itemParentName))
            extras.putString(EXTRA_ITEM_PARENT_NAME, itemParentName);
        if (!TextUtils.isEmpty(itemPosterName))
            extras.putString(EXTRA_ITEM_POSTER_NAME, itemPosterName);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ROOT_CALLER_ACTIVITY)) {
            mRootCallerActivity = intent.getStringExtra(EXTRA_ROOT_CALLER_ACTIVITY);
        }

        if (intent.hasExtra(EXTRA_ITEM)) {
            mItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));
            mParentId = mItem.getParent();
        }

        if (intent.hasExtra(EXTRA_ITEM_PARENT_NAME)) {
            mItemParentName = intent.getStringExtra(EXTRA_ITEM_PARENT_NAME);
        }

        if (intent.hasExtra(EXTRA_ITEM_POSTER_NAME)) {
            mItemPosterName = intent.getStringExtra(EXTRA_ITEM_POSTER_NAME);
        }
    }

    private void refresh() {
        getFragment().refresh();
    }

    private void share() {
        MenuUtils.openShareHackerNewsLinkChooser(this, mItem);
    }

    private void openPage() {
        CustomTabsBrowser.openTab(this, HackerNewsUtils.geItemUrl(mItem.getId()));
    }

    public static class StoryActivity extends ItemActivity {
        public static Intent createIntent(Context context) {
            return new Intent(context, StoryActivity.class);
        }
    }

    public static class CommentActivity extends ItemActivity {
        public static Intent createIntent(Context context) {
            return new Intent(context, CommentActivity.class);
        }
    }
}
