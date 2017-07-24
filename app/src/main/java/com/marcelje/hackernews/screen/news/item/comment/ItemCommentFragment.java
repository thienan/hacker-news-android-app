package com.marcelje.hackernews.screen.news.item.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.FragmentItemCommentBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.fragment.ToolbarFragment;
import com.marcelje.hackernews.listener.EndlessRecyclerViewScrollListener;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.utils.CollectionUtils;

import org.parceler.Parcels;

import java.util.List;

public class ItemCommentFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>>, View.OnClickListener {

    private static final String ARG_ITEM = "com.marcelje.hackernews.screen.news.item.arg.ITEM";
    private static final String ARG_PARENT = "com.marcelje.hackernews.screen.news.item.arg.PARENT";
    private static final String ARG_POSTER = "com.marcelje.hackernews.screen.news.items.arg.POSTER";

    private static final String STATE_COMMENT_DATA = "com.marcelje.hackernews.screen.news.item.state.COMMENT_DATA";
    private static final String STATE_CURRENT_PAGE = "com.marcelje.hackernews.screen.news.item.state.CURRENT_PAGE";

    private static final int LOADER_ID_COMMENT_ITEM = 400;

    private static final int ITEM_COUNT = 10;

    private Item mItem;
    private String mParent;
    private String mPoster;

    private FragmentItemCommentBinding mBinding;
    private CommentAdapter mAdapter;

    private int mCurrentPage = 1;

    public static ItemCommentFragment newInstance(Item item, String parent, String poster) {
        ItemCommentFragment fragment = new ItemCommentFragment();

        Bundle args = createArguments(item, parent, poster);
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
        mBinding = FragmentItemCommentBinding.inflate(inflater, container, false);
        mBinding.setTotal(mItem.getKids().size());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        if (TextUtils.isEmpty(mParent) && TextUtils.isEmpty(mPoster)) {
            mAdapter = new CommentAdapter(getToolbarActivity(), null, mItem.getBy());
        } else {
            mAdapter = new CommentAdapter(getToolbarActivity(), mItem.getBy(), mPoster);
        }

        mBinding.rvCommentList.setLayoutManager(layoutManager);
        mBinding.rvCommentList.setAdapter(mAdapter);
        mBinding.rvCommentList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mBinding.rvCommentList
                .addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        nextPageComments();
                    }
                });

        if (savedInstanceState == null) {
            refresh();
        } else {
            onRestoreInstanceState(savedInstanceState);
        }

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mAdapter.swapData((List<Item>) Parcels.unwrap(inState.getParcelable(STATE_COMMENT_DATA)));
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);

        if (mAdapter.getData().size() <= 0) refresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_COMMENT_DATA, Parcels.wrap(mAdapter.getData()));
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_COMMENT_ITEM:
                List<Long> kids = mItem.getKids();

                List<Long> list = CollectionUtils.subList(kids,
                        (mCurrentPage - 1) * ITEM_COUNT,
                        mCurrentPage * ITEM_COUNT);

                if (list.size() == 0) return null;

                return new ItemListLoader(getActivity(), list);
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader, HackerNewsResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_COMMENT_ITEM:
                    mAdapter.addData(response.getData());
                    hideProgressBar();
                    break;
                default:
            }
        } else {
            SnackbarFactory.createRetrieveErrorSnackbar(mBinding.getRoot(), this).show();
            hideProgressBar();
        }

    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    @Override
    public void onClick(View view) {
        retrieveComments();
    }

    public void refresh() {
        mAdapter.clearData();
        mCurrentPage = 1;
        showProgressBar();
        retrieveComments();
    }

    private void nextPageComments() {
        mCurrentPage++;
        retrieveComments();
    }

    private void retrieveComments() {
        if (mItem.getKids() == null) return;
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID_COMMENT_ITEM);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_COMMENT_ITEM, null, this);
    }

    private void showProgressBar() {
        mBinding.pbLoading.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.pbLoading.setVisibility(View.GONE);
    }

    private static Bundle createArguments(Item item, String parent, String poster) {
        Bundle args = new Bundle();
        if (item != null) args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) args.putString(ARG_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) args.putString(ARG_POSTER, poster);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_ITEM)) {
            mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        }

        if (args.containsKey(ARG_PARENT)) {
            mParent = args.getString(ARG_PARENT);
        }

        if (args.containsKey(ARG_POSTER)) {
            mPoster = args.getString(ARG_POSTER);
        }
    }
}
