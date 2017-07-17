package com.marcelje.hackernews.screen.news;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.database.HackerNewsContract;
import com.marcelje.hackernews.databinding.FragmentNewsBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.model.Item;

import java.util.List;

public class NewsActivityFragment extends Fragment
        implements HackerNewsApi.RestCallback<List<Long>>,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TYPE_TOP = "Top";
    public static final String TYPE_BEST = "Best";
    public static final String TYPE_NEW = "New";
    public static final String TYPE_SHOW = "Show";
    public static final String TYPE_ASK = "Ask";
    public static final String TYPE_JOB = "Jobs";
    public static final String TYPE_BOOKMARKED = "Bookmarked";

    private static final int LOADER_BOOKMARKED_ITEM_ID = 111;

    private ToolbarActivity mActivity;

    private FragmentNewsBinding mBinding;
    private NewsAdapter mAdapter;

    private String mType = TYPE_TOP;
    private List<Long> mItems;

    public static NewsActivityFragment newInstance() {
        return new NewsActivityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = ToolbarActivity.getActivity(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NewsAdapter(mActivity);

        mBinding.rvItemList.setLayoutManager(layoutManager);
        mBinding.rvItemList.setAdapter(mAdapter);

        mBinding.srlRefresh.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlRefresh.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            retrieveNews(mType);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onSuccess(List<Long> data) {
        hideProgressBar();

        mAdapter.clearData();
        mItems = data;

        retrieveItems(mType);
    }

    @Override
    public void onFailure(String message) {
        hideProgressBar();

        SnackbarFactory
                .createRetrieveErrorSnackbar(mBinding.rvItemList,
                        NewsActivityFragment.this).show();
    }

    @Override
    public void onRefresh() {
        retrieveNews(mType);
    }

    @Override
    public void onClick(View view) {
        retrieveNews(mType);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                HackerNewsContract.BookmarkedItemEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Item> items = Item.Factory.fromCursor(data);
        mAdapter.swapData(items);
        hideProgressBar();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void retrieveNews(String type) {
        mType = type;

        showProgressBar();

        switch (type) {
            case TYPE_TOP:
                HackerNewsApi.with(getActivity()).getTopStories(this);
                getActivity().getSupportLoaderManager().destroyLoader(LOADER_BOOKMARKED_ITEM_ID);
                break;
            case TYPE_BEST:
                HackerNewsApi.with(getActivity()).getBestStories(this);
                getActivity().getSupportLoaderManager().destroyLoader(LOADER_BOOKMARKED_ITEM_ID);
                break;
            case TYPE_NEW:
                HackerNewsApi.with(getActivity()).getNewStories(this);
                getActivity().getSupportLoaderManager().destroyLoader(LOADER_BOOKMARKED_ITEM_ID);
                break;
            case TYPE_SHOW:
                HackerNewsApi.with(getActivity()).getShowStories(this);
                getActivity().getSupportLoaderManager().destroyLoader(LOADER_BOOKMARKED_ITEM_ID);
                break;
            case TYPE_ASK:
                HackerNewsApi.with(getActivity()).getAskStories(this);
                getActivity().getSupportLoaderManager().destroyLoader(LOADER_BOOKMARKED_ITEM_ID);
                break;
            case TYPE_JOB:
                HackerNewsApi.with(getActivity()).getJobStories(this);
                getActivity().getSupportLoaderManager().destroyLoader(LOADER_BOOKMARKED_ITEM_ID);
                break;
            case TYPE_BOOKMARKED:
                getActivity().getSupportLoaderManager().restartLoader(LOADER_BOOKMARKED_ITEM_ID, null, this);
                break;
            default:
                hideProgressBar();
                break;
        }
    }

    private void retrieveItems(final String type) {
        for (long itemId : mItems) {
            HackerNewsApi.with(getActivity()).getItem(itemId, new HackerNewsApi.RestCallback<Item>() {
                @Override
                public void onSuccess(Item data) {
                    if (data.isNotDeleted() && data.isNotDead()) {
                        if (type.equals(mType)) mAdapter.addData(data);
                    }
                }

                @Override
                public void onFailure(String message) {
                    //do nothing
                }
            });
        }
    }

    private void showProgressBar() {
        mBinding.pbLoading.setVisibility(View.VISIBLE);
        mBinding.rvItemList.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        mBinding.pbLoading.setVisibility(View.GONE);
        mBinding.rvItemList.setVisibility(View.VISIBLE);
        mBinding.srlRefresh.setRefreshing(false);
    }
}
