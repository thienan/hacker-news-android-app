package com.marcelljee.hackernews.screen.news.item.head;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.databinding.FragmentCommentBinding;
import com.marcelljee.hackernews.event.ItemRefreshEvent;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.loader.HackerNewsResponse;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.viewmodel.FragmentCommentViewModel;
import com.marcelljee.hackernews.viewmodel.ItemCommentViewModel;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

public class CommentFragment extends ItemHeadFragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.head.arg.ITEM";
    private static final String ARG_ITEM_PARENT_NAME = "com.marcelljee.hackernews.screen.news.item.head.arg.ITEM_PARENT_NAME";
    private static final String ARG_ITEM_POSTER_NAME = "com.marcelljee.hackernews.screen.news.item.head.arg.ITEM_POSTER_NAME";

    private static final int LOADER_ID_COMMENT_HEAD = 100;

    private Item mItem;
    private String mItemParentName;
    private String mItemPosterName;

    private FragmentCommentBinding mBinding;

    public static CommentFragment newInstance(Item item, String itemParentName, String itemPosterName) {
        CommentFragment fragment = new CommentFragment();

        Bundle args = createArguments(item, itemParentName, itemPosterName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCommentBinding.inflate(inflater, container, false);
        mBinding.setViewModel(new FragmentCommentViewModel(getToolbarActivity(), mItemParentName, mItemPosterName));
        mBinding.setItem(mItem);

        mBinding.tvCommentInfo.setMovementMethod(LinkMovementMethod.getInstance());

        mBinding.commentHead.setViewModel(new ItemCommentViewModel(getToolbarActivity()));
        mBinding.commentHead.tvText.setMaxLines(Integer.MAX_VALUE);
        mBinding.commentHead.tvText.setMovementMethod(LinkMovementMethod.getInstance());

        return mBinding.getRoot();
    }

    @Override
    public void refresh() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_COMMENT_HEAD, null, this);
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_COMMENT_HEAD:
                return new ItemListLoader(getContext(), Collections.singletonList(mItem.getId()));
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader,
                               HackerNewsResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_COMMENT_HEAD:
                    mItem = response.getData().get(0);
                    mBinding.setItem(mItem);
                    EventBus.getDefault().post(new ItemRefreshEvent(mItem));
                    break;
                default:
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    private static Bundle createArguments(Item item, String itemParentName, String itemPosterName) {
        Bundle args = new Bundle();
        if (item != null) args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(itemParentName)) args.putString(ARG_ITEM_PARENT_NAME, itemParentName);
        if (!TextUtils.isEmpty(itemPosterName)) args.putString(ARG_ITEM_POSTER_NAME, itemPosterName);

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
}
