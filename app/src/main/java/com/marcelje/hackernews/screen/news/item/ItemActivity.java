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
import com.marcelje.hackernews.screen.news.NewsActivity;
import com.marcelje.hackernews.screen.news.item.head.CommentFragment;
import com.marcelje.hackernews.screen.news.item.head.StoryFragment;
import com.marcelje.hackernews.utils.BrowserUtils;
import com.marcelje.hackernews.utils.HackerNewsUtils;
import com.marcelje.hackernews.utils.ItemUtils;
import com.marcelje.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

public class ItemActivity extends ToolbarActivity
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String EXTRA_CALLER_ACTIVITY = "com.marcelje.hackernews.screen.news.item.extra.CALLER_ACTIVITY";
    private static final String EXTRA_ITEM_ID = "com.marcelje.hackernews.screen.news.item.extra.ITEM_ID";
    public static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.item.extra.ITEM";
    private static final String EXTRA_PARENT = "com.marcelje.hackernews.screen.news.item.extra.PARENT";
    private static final String EXTRA_POSTER = "com.marcelje.hackernews.screen.news.item.extra.POSTER";

    private static final String ITEM_TYPE_COMMENT = "comment";
    private static final String ITEM_TYPE_STORY = "story";
    private static final String ITEM_TYPE_POLL = "poll";
    private static final String ITEM_TYPE_JOB = "job";

    private static final int LOADER_ID_ITEM = 300;

    private ItemHeadFragment mHeadFragment;
    private ItemCommentFragment mCommentFragment;

    private String mCallerActivity;
    private long mItemId;
    private Item mItem;
    private String mParent;
    private String mPoster;

    public static Intent createIntent(Context context) {
        return new Intent(context, ItemActivity.class);
    }

    public static void startActivity(ToolbarActivity activity, Item item) {
        startActivity(activity, item, null, null);
    }

    public static void startActivity(ToolbarActivity activity, Item item, String parent, String poster) {
        startActivity(activity, createExtras(activity, item, parent, poster));
    }

    private static void startActivity(ToolbarActivity activity, long itemId) {
        startActivity(activity, createExtras(activity, itemId));
    }

    private static void startActivity(ToolbarActivity activity, Bundle extras) {
        Intent intent = new Intent(activity, ItemActivity.class);
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setDisplayHomeAsUpEnabled(true);

        extractExtras();

        if (mItem != null) {
            init();
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID_ITEM, null, this);
        }
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

        if (HackerNewsDao.isItemAvailable(this, mItemId)) {
            menuItemBookmark.setTitle(R.string.menu_item_unbookmark);
        } else {
            menuItemBookmark.setTitle(R.string.menu_item_bookmark);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (NewsActivity.class.getName().equals(mCallerActivity) ||
                        mItem.getParent() <= 0) {

                    return super.onOptionsItemSelected(menuItem);
                } else {
                    startActivity(this, mItem.getParent());
                    return true;
                }
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
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_ITEM:
                return new ItemListLoader(this, Collections.singletonList(mItemId));
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader,
                               HackerNewsResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_ITEM:
                    mItem = response.getData().get(0);
                    init();
                    break;
                default:
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    private void init() {
        setTitle(ItemUtils.getTypeAsTitle(mItem));

        switch (mItem.getType()) {
            case ITEM_TYPE_COMMENT:
                mHeadFragment = CommentFragment.newInstance(mItem, mParent, mPoster);
                break;
            case ITEM_TYPE_STORY:
                //fall through
            case ITEM_TYPE_JOB:
                //fall through
            case ITEM_TYPE_POLL:
                //fall through
            default:
                mHeadFragment = StoryFragment.newInstance(mItem);
        }

        mCommentFragment = ItemCommentFragment.newInstance(mItem, mParent, mPoster);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_head_container, mHeadFragment)
                .add(R.id.item_comment_container, mCommentFragment)
                .commitAllowingStateLoss(); //TODO: not a good solution
    }

    private static Bundle createExtras(Activity activity, long itemId) {
        Bundle extras = new Bundle();
        if (activity != null)
            extras.putString(EXTRA_CALLER_ACTIVITY, activity.getClass().getName());
        if (itemId > 0) extras.putLong(EXTRA_ITEM_ID, itemId);

        return extras;
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

        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            mItemId = intent.getLongExtra(EXTRA_ITEM_ID, -1);
        }

        if (intent.hasExtra(EXTRA_ITEM)) {
            mItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));
            mItemId = mItem.getId();
        }

        if (intent.hasExtra(EXTRA_PARENT)) {
            mParent = intent.getStringExtra(EXTRA_PARENT);
        }

        if (intent.hasExtra(EXTRA_POSTER)) {
            mPoster = intent.getStringExtra(EXTRA_POSTER);
        }
    }

    private void refresh() {
        mHeadFragment.refresh();
        mCommentFragment.refresh();
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
