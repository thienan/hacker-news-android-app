package com.marcelljee.hackernews.screen.news.item;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.comment.ItemCommentFragment;
import com.marcelljee.hackernews.screen.news.item.head.CommentFragment;
import com.marcelljee.hackernews.screen.news.item.head.ItemHeadFragment;
import com.marcelljee.hackernews.screen.news.item.head.StoryFragment;

import org.parceler.Parcels;

public class ItemFragment extends Fragment {

    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.ITEM";
    private static final String ARG_ITEM_PARENT_NAME = "com.marcelljee.hackernews.screen.news.item.arg.ITEM_PARENT_NAME";
    private static final String ARG_ITEM_POSTER_NAME = "com.marcelljee.hackernews.screen.news.item.arg.ITEM_POSTER_NAME";

    private static final String TAG_HEAD_FRAGMENT = "com.marcelljee.hackernews.screen.news.item.tag.HEAD_FRAGMENT";
    private static final String TAG_COMMENT_FRAGMENT = "com.marcelljee.hackernews.screen.news.item.tag.COMMENT_FRAGMENT";

    private Item mItem;
    private String mItemParentName;
    private String mItemPosterName;

    public static ItemFragment newInstance(Item item, String itemParentName, String itemPosterName) {
        ItemFragment fragment = new ItemFragment();

        Bundle args = createArguments(item, itemParentName, itemPosterName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();

        switch (mItem.getType()) {
            case ItemActivity.ITEM_TYPE_COMMENT:
                loadFragment(CommentFragment.newInstance(mItem, mItemParentName, mItemPosterName));
                break;
            case ItemActivity.ITEM_TYPE_STORY:
            case ItemActivity.ITEM_TYPE_POLL:
            case ItemActivity.ITEM_TYPE_JOB:
            default:
                loadFragment(StoryFragment.newInstance(mItem));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    private static Bundle createArguments(Item item, String itemParentName, String itemPosterName) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        args.putString(ARG_ITEM_PARENT_NAME, itemParentName);
        args.putString(ARG_ITEM_POSTER_NAME, itemPosterName);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_ITEM)) {
            mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        }

        if (args.containsKey(ARG_ITEM_PARENT_NAME)) {
            mItemParentName = args.getString(ARG_ITEM_PARENT_NAME);
        }

        if (args.containsKey(ARG_ITEM_POSTER_NAME)) {
            mItemPosterName = args.getString(ARG_ITEM_POSTER_NAME);
        }
    }

    private void loadFragment(ItemHeadFragment headFragment) {
        ItemCommentFragment commentFragment = ItemCommentFragment
                .newInstance(mItem, mItemParentName, mItemPosterName);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.item_head_container, headFragment, TAG_HEAD_FRAGMENT)
                .add(R.id.item_comment_container, commentFragment, TAG_COMMENT_FRAGMENT)
                .commit();
    }

    private ItemHeadFragment getHeadFragment() {
        return (ItemHeadFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag(TAG_HEAD_FRAGMENT);
    }

    private ItemCommentFragment getCommentFragment() {
        return (ItemCommentFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag(TAG_COMMENT_FRAGMENT);
    }

    public void refresh() {
        getHeadFragment().refresh();
        getCommentFragment().refresh();
    }
}
