package com.marcelljee.hackernews.screen.news.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.databinding.ActivityItemBinding;
import com.marcelljee.hackernews.databinding.viewmodel.ItemViewModel;
import com.marcelljee.hackernews.event.ItemUpdateEvent;
import com.marcelljee.hackernews.loader.AppResponse;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.screen.news.NewsActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.utils.CollectionUtils;
import com.marcelljee.hackernews.utils.HackerNewsUtils;
import com.marcelljee.hackernews.utils.ItemUtils;
import com.marcelljee.hackernews.utils.MenuUtils;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.List;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;

public class ItemActivity extends ToolbarActivity
        implements LoaderManager.LoaderCallbacks<AppResponse<List<Item>>> {

    private static final String EXTRA_ROOT_CALLER_ACTIVITY = "com.marcelljee.hackernews.screen.news.item.extra.ROOT_CALLER_ACTIVITY";
    public static final String EXTRA_ITEMS = "com.marcelljee.hackernews.screen.news.item.extra.ITEMS";
    public static final String EXTRA_ITEM_POSITION = "com.marcelljee.hackernews.screen.news.item.extra.ITEM_POSITION";
    private static final String EXTRA_PARENT_ITEM = "com.marcelljee.hackernews.screen.news.item.extra.PARENT_ITEM";
    private static final String EXTRA_POSTER_ITEM = "com.marcelljee.hackernews.screen.news.item.extra.POSTER_ITEM";

    private static final String STATE_ITEM_POSITION = "com.marcelljee.hackernews.screen.news.item.state.ITEM_POSITION";
    private static final String STATE_PARENT_ID = "com.marcelljee.hackernews.screen.news.item.state.PARENT_ID";
    private static final String STATE_PARENT_ITEM = "com.marcelljee.hackernews.screen.news.item.state.PARENT_ITEM";

    public static final String ITEM_TYPE_COMMENT = "comment";
    public static final String ITEM_TYPE_STORY = "story";
    public static final String ITEM_TYPE_POLL = "poll";
    public static final String ITEM_TYPE_JOB = "job";

    private static final int LOADER_PARENT_ITEM = 5000;

    private String mRootCallerActivity;
    private List<Item> mItems;

    private int mItemPosition;
    private long mParentId;
    private Item mParentItem;

    private ItemPagerAdapter mItemPagerAdapter;

    public static void startActivity(ToolbarActivity activity, List<Item> items, int itemPosition) {
        startActivity(activity, items, itemPosition, null, null);
    }

    public static void startActivity(ToolbarActivity activity, List<Item> items, int itemPosition,
                                     Item parentItem, Item posterItem) {
        switch (items.get(itemPosition).getType()) {
            case ITEM_TYPE_COMMENT:
                startActivity(activity,
                        CommentActivity.createIntent(activity), items, itemPosition, parentItem, posterItem);
                break;
            case ITEM_TYPE_STORY:
            case ITEM_TYPE_POLL:
            case ITEM_TYPE_JOB:
            default:
                startActivity(activity,
                        StoryActivity.createIntent(activity), items, itemPosition, parentItem, posterItem);
                break;
        }
    }

    private static void startActivity(ToolbarActivity activity, Intent intent, List<Item> items, int itemPosition,
                                      Item parentItem, Item posterItem) {
        Bundle extras = createExtras(activity, items, itemPosition, parentItem, posterItem);
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityItemBinding binding = setContentViewBinding(R.layout.activity_item);
        setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mRootCallerActivity = intent.getStringExtra(EXTRA_ROOT_CALLER_ACTIVITY);
        mItemPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, PagerAdapter.POSITION_NONE);
        mItems = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEMS));
        mParentId = getItem().getParent();
        mParentItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_PARENT_ITEM));
        Item posterItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_POSTER_ITEM));

        setTitle(ItemUtils.getTypeAsTitle(getItem()));

        if (mParentItem == null && posterItem == null) {
            binding.tvCommentInfo.setVisibility(View.GONE);
        } else {
            binding.itemParent.setItem(mParentItem);
            binding.itemPoster.setItem(posterItem);

            binding.itemParent.commentHead.setViewModel(new ItemViewModel(this,
                    CollectionUtils.singleItemList(mParentItem)));
            binding.itemParent.commentHead.tvCommentText.setMaxLines(Integer.MAX_VALUE);

            binding.itemPoster.itemNews.setViewModel(new ItemViewModel(this,
                    CollectionUtils.singleItemList(posterItem)));
            binding.itemPoster.itemNews.setItemPosition(0);

            binding.tvCommentInfo.setVisibility(View.VISIBLE);
            binding.tvCommentInfo.setText(ItemUtils.getCommentInfo(this,
                    mParentItem, binding.itemParent.getRoot(),
                    posterItem, binding.itemPoster.getRoot()));
            binding.tvCommentInfo.setMovementMethod(LinkMovementMethod.getInstance());
        }

        mItemPagerAdapter = new ItemPagerAdapter(binding.itemPager,
                getSupportFragmentManager(), mItems, posterItem);

        binding.itemPager.setAdapter(mItemPagerAdapter);
        binding.itemPager.setCurrentItem(mItemPosition);
        binding.itemPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mItemPosition = position;

                Item item = mItems.get(position);
                item.setRead(true);
                EventBus.getDefault().post(new ItemUpdateEvent(item));
            }
        });

        if (savedInstanceState == null) {
            loadParentItem();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mItemPosition = savedInstanceState.getInt(STATE_ITEM_POSITION);
        mParentId = savedInstanceState.getLong(STATE_PARENT_ID);
        mParentItem = Parcels.unwrap(savedInstanceState.getParcelable(STATE_PARENT_ITEM));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_ITEM_POSITION, PagerAdapter.POSITION_NONE);
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
                        && getItem().getParent() > 0) {
                    if (mParentItem != null) {
                        ItemActivity.startActivity(this,
                                CollectionUtils.singleItemList(mParentItem), 0);
                    } else {
                        loadParentItem();
                    }

                    return true;
                }

                return super.onOptionsItemSelected(menuItem);
            case R.id.action_refresh:
                mItemPagerAdapter.getCurrentFragment().refresh();
                return true;
            case R.id.action_share:
                MenuUtils.openShareHackerNewsLinkChooser(this, getItem());
                return true;
            case R.id.action_open_page:
                CustomTabsBrowser.openTab(this, HackerNewsUtils.getItemUrl(getItem().getId()));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public Loader<AppResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_PARENT_ITEM:
                return new ItemListLoader(this, CollectionUtils.singleItemList(mParentId));
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<AppResponse<List<Item>>> loader,
                               AppResponse<List<Item>> response) {
        if (response != null && response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_PARENT_ITEM:
                    mParentItem = response.getData().get(0);
                    break;
                default:
                    //do nothing
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<AppResponse<List<Item>>> loader) {

    }

    private void loadParentItem() {
        if (mParentItem != null) return;
        mParentId = getItem().getParent();

        if (mParentId > 0) {
            getSupportLoaderManager().restartLoader(LOADER_PARENT_ITEM, null, this);
        }
    }

    private static Bundle createExtras(Activity activity, List<Item> items, int itemPosition,
                                       Item parentItem, Item posterItem) {
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

        if (itemPosition >= 0) extras.putInt(EXTRA_ITEM_POSITION, itemPosition);
        if (items != null) extras.putParcelable(EXTRA_ITEMS, Parcels.wrap(items));
        if (parentItem != null) extras.putParcelable(EXTRA_PARENT_ITEM, Parcels.wrap(parentItem));
        if (posterItem != null) extras.putParcelable(EXTRA_POSTER_ITEM, Parcels.wrap(posterItem));

        return extras;
    }

    private Item getItem() {
        return mItems.get(mItemPosition);
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
