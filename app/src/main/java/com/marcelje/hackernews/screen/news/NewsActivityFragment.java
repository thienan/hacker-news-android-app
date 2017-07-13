package com.marcelje.hackernews.screen.news;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.databinding.FragmentNewsBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.model.Item;

import java.util.List;

public class NewsActivityFragment extends Fragment
        implements HackerNewsApi.RestCallback<List<Long>>,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {

    public static final String TYPE_TOP = "Top";
    public static final String TYPE_BEST = "Best";
    public static final String TYPE_NEW = "New";
    public static final String TYPE_SHOW = "Show";
    public static final String TYPE_ASK = "Ask";
    public static final String TYPE_JOB = "Jobs";

    private FragmentNewsBinding mBinding;
    private NewsAdapter mAdapter;

    private String mType = TYPE_TOP;
    private List<Long> mItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NewsAdapter();

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

        retrieveItems();
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

    public void retrieveNews(String type) {
        mType = type;

        showProgressBar();

        switch (type) {
            case TYPE_TOP:
                HackerNewsApi.with(getActivity()).getTopStories(this);
                break;
            case TYPE_BEST:
                HackerNewsApi.with(getActivity()).getBestStories(this);
                break;
            case TYPE_NEW:
                HackerNewsApi.with(getActivity()).getNewStories(this);
                break;
            case TYPE_SHOW:
                HackerNewsApi.with(getActivity()).getShowStories(this);
                break;
            case TYPE_ASK:
                HackerNewsApi.with(getActivity()).getAskStories(this);
                break;
            case TYPE_JOB:
                HackerNewsApi.with(getActivity()).getJobStories(this);
                break;
            default:
                hideProgressBar();
                break;
        }
    }

    private void retrieveItems() {
        for (long itemId : mItems) {
            HackerNewsApi.with(getActivity()).getItem(itemId, new HackerNewsApi.RestCallback<Item>() {
                @Override
                public void onSuccess(Item data) {
                    mAdapter.addData(data);
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
    }

    private void hideProgressBar() {
        mBinding.pbLoading.setVisibility(View.GONE);
        mBinding.srlRefresh.setRefreshing(false);
    }
}
