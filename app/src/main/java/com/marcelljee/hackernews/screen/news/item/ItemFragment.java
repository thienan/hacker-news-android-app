package com.marcelljee.hackernews.screen.news.item;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.comment.ItemCommentFragment;
import com.marcelljee.hackernews.screen.news.item.head.CommentFragment;
import com.marcelljee.hackernews.screen.news.item.head.ItemHeadFragment;
import com.marcelljee.hackernews.screen.news.item.head.StoryFragment;

import org.parceler.Parcels;

public class ItemFragment extends ToolbarFragment {

    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.ITEM";
    private static final String ARG_ITEM_POSTER_NAME = "com.marcelljee.hackernews.screen.news.item.arg.ITEM_POSTER_NAME";
    private static final String ARG_ITEM_LOADER_OFFSET = "com.marcelljee.hackernews.screen.news.item.arg.ITEM_LOADER_OFFSET";

    private static final String TAG_HEAD_FRAGMENT = "com.marcelljee.hackernews.screen.news.item.tag.HEAD_FRAGMENT";
    private static final String TAG_COMMENT_FRAGMENT = "com.marcelljee.hackernews.screen.news.item.tag.COMMENT_FRAGMENT";

    private Item mItem;
    private String mItemPosterName;
    private int mLoaderOffset;

    public static ItemFragment newInstance(Item item, String itemPosterName, int loaderOffset) {
        ItemFragment fragment = new ItemFragment();

        Bundle args = createArguments(item, itemPosterName, loaderOffset);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();

        if (savedInstanceState == null) {
            switch (mItem.getType()) {
                case ItemActivity.ITEM_TYPE_COMMENT:
                    loadFragment(CommentFragment.newInstance(mItem));
                    break;
                case ItemActivity.ITEM_TYPE_STORY:
                case ItemActivity.ITEM_TYPE_POLL:
                case ItemActivity.ITEM_TYPE_JOB:
                default:
                    loadFragment(StoryFragment.newInstance(mItem));
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    private static Bundle createArguments(Item item, String itemPosterName, int loaderOffset) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        args.putString(ARG_ITEM_POSTER_NAME, itemPosterName);
        args.putInt(ARG_ITEM_LOADER_OFFSET, loaderOffset);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_ITEM)) {
            mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        }

        if (args.containsKey(ARG_ITEM_POSTER_NAME)) {
            mItemPosterName = args.getString(ARG_ITEM_POSTER_NAME);
        }

        if (args.containsKey(ARG_ITEM_LOADER_OFFSET)) {
            mLoaderOffset = args.getInt(ARG_ITEM_LOADER_OFFSET);
        }
    }

    private void loadFragment(ItemHeadFragment headFragment) {
        ItemCommentFragment commentFragment = ItemCommentFragment.newInstance(mItem, mItemPosterName, mLoaderOffset);

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
