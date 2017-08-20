package com.marcelljee.hackernews.screen.news.item;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.event.ItemUpdateEvent;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.comment.ItemCommentFragment;
import com.marcelljee.hackernews.screen.news.item.head.CommentFragment;
import com.marcelljee.hackernews.screen.news.item.head.ItemHeadFragment;
import com.marcelljee.hackernews.screen.news.item.head.StoryFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

public class ItemFragment extends ToolbarFragment {

    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.ITEM";
    private static final String ARG_POSTER_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.POSTER_ITEM";
    private static final String ARG_ITEM_LOADER_OFFSET = "com.marcelljee.hackernews.screen.news.item.arg.ITEM_LOADER_OFFSET";

    private static final String STATE_ITEM = "com.marcelljee.hackernews.screen.news.item.state.ITEM";

    private static final String TAG_HEAD_FRAGMENT = "com.marcelljee.hackernews.screen.news.item.tag.HEAD_FRAGMENT";
    private static final String TAG_COMMENT_FRAGMENT = "com.marcelljee.hackernews.screen.news.item.tag.COMMENT_FRAGMENT";

    private Item mItem;

    public static ItemFragment newInstance(Item item, Item posterItem, int loaderOffset) {
        ItemFragment fragment = new ItemFragment();

        Bundle args = createArguments(item, posterItem, loaderOffset);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        Item posterItem = Parcels.unwrap(args.getParcelable(ARG_POSTER_ITEM));
        int loaderOffset = args.getInt(ARG_ITEM_LOADER_OFFSET);

        if (savedInstanceState == null) {
            switch (mItem.getType()) {
                case ItemActivity.ITEM_TYPE_COMMENT:
                    loadFragment(CommentFragment.newInstance(mItem), posterItem, loaderOffset);
                    break;
                case ItemActivity.ITEM_TYPE_STORY:
                case ItemActivity.ITEM_TYPE_POLL:
                case ItemActivity.ITEM_TYPE_JOB:
                default:
                    loadFragment(StoryFragment.newInstance(mItem), posterItem, loaderOffset);
                    break;
            }
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void onRestoreInstanceState(Bundle inState) {
        if (mItem != null) {
            mItem.update(Parcels.unwrap(inState.getParcelable(STATE_ITEM)));
        } else {
            mItem = Parcels.unwrap(inState.getParcelable(STATE_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_ITEM, Parcels.wrap(mItem));
        super.onSaveInstanceState(outState);
    }

    @Subscribe()
    @SuppressWarnings({"unused"})
    public void onItemUpdateEvent(ItemUpdateEvent event) {
        if (mItem != null) {
            mItem.update(event.getItem());
        } else {
            mItem = event.getItem();
        }
    }

    private static Bundle createArguments(Item item, Item posterItem, int loaderOffset) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        args.putParcelable(ARG_POSTER_ITEM, Parcels.wrap(posterItem));
        args.putInt(ARG_ITEM_LOADER_OFFSET, loaderOffset);

        return args;
    }

    private void loadFragment(ItemHeadFragment headFragment, Item posterItem, int loaderOffset) {
        ItemCommentFragment commentFragment = ItemCommentFragment.newInstance(loaderOffset, mItem, posterItem);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.item_head_container, headFragment, TAG_HEAD_FRAGMENT)
                .replace(R.id.item_comment_container, commentFragment, TAG_COMMENT_FRAGMENT)
                .commit();
    }

    private ItemHeadFragment getHeadFragment() {
        return (ItemHeadFragment) getChildFragmentManager().findFragmentByTag(TAG_HEAD_FRAGMENT);
    }

    public void refresh() {
        getHeadFragment().refresh();
    }
}
