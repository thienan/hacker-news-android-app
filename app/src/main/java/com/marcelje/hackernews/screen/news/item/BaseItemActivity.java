package com.marcelje.hackernews.screen.news.item;

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

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.utils.BrowserUtils;
import com.marcelje.hackernews.utils.HackerNewsUtils;
import com.marcelje.hackernews.utils.ItemUtils;
import com.marcelje.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

public class BaseItemActivity extends ToolbarActivity
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String EXTRA_CALLER_ACTIVITY = "com.marcelje.hackernews.screen.news.item.extra.CALLER_ACTIVITY";
    public static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.item.extra.ITEM";
    private static final String EXTRA_PARENT = "com.marcelje.hackernews.screen.news.item.extra.PARENT";
    private static final String EXTRA_POSTER = "com.marcelje.hackernews.screen.news.item.extra.POSTER";

    private static final String TAG_HEAD_FRAGMENT = "com.marcelje.hackernews.screen.news.item.tag.HEAD_FRAGMENT";
    private static final String TAG_COMMENT_FRAGMENT = "com.marcelje.hackernews.screen.news.item.tag.COMMENT_FRAGMENT";

    private static final String STATE_PARENT_ID = "com.marcelje.hackernews.screen.news.item.state.PARENT_ID";
    private static final String STATE_PARENT_ITEM = "com.marcelje.hackernews.screen.news.item.state.PARENT_ITEM";

    protected static final String ITEM_TYPE_COMMENT = "comment";
    private static final String ITEM_TYPE_STORY = "story";
    private static final String ITEM_TYPE_POLL = "poll";
    private static final String ITEM_TYPE_JOB = "job";

    private static final int LOADER_PARENT_ITEM = 300;

    protected String mCallerActivity;
    protected Item mItem;
    protected String mParent;
    protected String mPoster;

    private long mParentId;
    protected Item mParentItem;

    public static void startActivity(ToolbarActivity activity, Item item) {
        startActivity(activity, item, null, null);
    }

    public static void startActivity(ToolbarActivity activity, Item item, String parent, String poster) {
        if (ITEM_TYPE_COMMENT.equals(item.getType())) {
            CommentActivity.startActivity(activity, item, parent, poster);
        } else {
            StoryActivity.startActivity(activity, item, parent, poster);
        }
    }

    protected static void startActivity(ToolbarActivity activity, Intent intent, Item item, String parent, String poster) {
        Bundle extras = createExtras(activity, item, parent, poster);
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setDisplayHomeAsUpEnabled(true);

        extractExtras();

        setTitle(ItemUtils.getTypeAsTitle(mItem));

        if (savedInstanceState == null) {
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemBookmark = menu.findItem(R.id.action_bookmark);

        if (mItem != null && ITEM_TYPE_COMMENT.equals(mItem.getType())) {
            menuItemBookmark.setVisible(false);
        }

        if (HackerNewsDao.isItemAvailable(this, mItem.getId())) {
            menuItemBookmark.setTitle(R.string.menu_item_unbookmark);
        } else {
            menuItemBookmark.setTitle(R.string.menu_item_bookmark);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_bookmark:
                bookmark(menuItem);
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

    protected void loadFragment(ItemHeadFragment headFragment, ItemCommentFragment commentFragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_head_container, headFragment, TAG_HEAD_FRAGMENT)
                .add(R.id.item_comment_container, commentFragment, TAG_COMMENT_FRAGMENT)
                .commit();
    }

    protected void loadParentItem() {
        mParentId = mItem.getParent();

        if (mParentId > 0) {
            getSupportLoaderManager().restartLoader(LOADER_PARENT_ITEM, null, this);
        }
    }

    private static Bundle createExtras(Activity activity, Item item, String parent, String poster) {
        Bundle extras = new Bundle();
        if (activity != null)
            extras.putString(EXTRA_CALLER_ACTIVITY, activity.getClass().getName());
        if (item != null) extras.putParcelable(EXTRA_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) extras.putString(EXTRA_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) extras.putString(EXTRA_POSTER, poster);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_CALLER_ACTIVITY)) {
            mCallerActivity = intent.getStringExtra(EXTRA_CALLER_ACTIVITY);
        }

        if (intent.hasExtra(EXTRA_ITEM)) {
            mItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));
            mParentId = mItem.getParent();
        }

        if (intent.hasExtra(EXTRA_PARENT)) {
            mParent = intent.getStringExtra(EXTRA_PARENT);
        }

        if (intent.hasExtra(EXTRA_POSTER)) {
            mPoster = intent.getStringExtra(EXTRA_POSTER);
        }
    }

    private void refresh() {
        ((ItemHeadFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_HEAD_FRAGMENT)).refresh();
        ((ItemCommentFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_COMMENT_FRAGMENT)).refresh();
    }

    private void share() {
        MenuUtils.openShareHackerNewsLinkChooser(this, mItem);
    }

    private void bookmark(MenuItem menuItem) {
        if (HackerNewsDao.isItemAvailable(this, mItem.getId())) {
            HackerNewsDao.deleteItem(this, mItem.getId());
            SnackbarFactory.createUnbookmarkedSuccessSnackBar(getToolbar()).show();
            menuItem.setTitle(R.string.menu_item_bookmark);
        } else {
            HackerNewsDao.insertItem(this, mItem);
            SnackbarFactory.createBookmarkedSuccessSnackBar(getToolbar()).show();
            menuItem.setTitle(R.string.menu_item_unbookmark);
        }
    }

    private void openPage() {
        BrowserUtils.openTab(this, HackerNewsUtils.geItemUrl(mItem.getId()));
    }
}
