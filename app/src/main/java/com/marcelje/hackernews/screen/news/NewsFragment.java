package com.marcelje.hackernews.screen.news;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.fragment.ToolbarFragment;
import com.marcelje.hackernews.listener.EndlessRecyclerViewScrollListener;
import com.marcelje.hackernews.R;
import com.marcelje.hackernews.databinding.FragmentNewsBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.loader.BookmarkedItemLoader;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.loader.StoriesLoader;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.utils.CollectionUtils;

import java.util.List;

public class NewsFragment extends ToolbarFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String TYPE_TOP = "Top";
    private static final String TYPE_BEST = "Best";
    private static final String TYPE_NEW = "New";
    private static final String TYPE_SHOW = "Show";
    private static final String TYPE_ASK = "Ask";
    private static final String TYPE_JOB = "Jobs";
    private static final String TYPE_BOOKMARKED = "Bookmarked";

    private static final int LOADER_ID_STORIES = 500;
    private static final int LOADER_ID_STORIES_ITEM = 600;
    private static final int LOADER_ID_BOOKMARKED_ITEM = 700;

    private static final int ITEM_COUNT = 10;
    private int mCurrentPage = 1;

    private FragmentNewsBinding mBinding;
    private NewsAdapter mAdapter;

    private List<Long> mItemIds;
    private String mNewsType;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NewsAdapter(getToolbarActivity());

        mBinding.rvItemList.setLayoutManager(layoutManager);
        mBinding.rvItemList.setAdapter(mAdapter);
        mBinding.rvItemList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!TYPE_BOOKMARKED.equals(mNewsType)) {
                    nextPageNews();
                }
            }
        });

        mBinding.srlRefresh.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlRefresh.setOnRefreshListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onRefresh() {
        refreshNews();
    }

    @Override
    public void onClick(View view) {
        retrieveNews();
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_STORIES_ITEM:
                List<Long> list = CollectionUtils.subList(mItemIds,
                        (mCurrentPage - 1) * ITEM_COUNT,
                        mCurrentPage * ITEM_COUNT);

                if (list.size() == 0) return null;

                return new ItemListLoader(getActivity(), list);
            case LOADER_ID_BOOKMARKED_ITEM:
                return new BookmarkedItemLoader(getActivity());
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader,
                               HackerNewsResponse<List<Item>> data) {
        if (data.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_STORIES_ITEM:
                    mAdapter.addData(data.getData());
                    break;
                case LOADER_ID_BOOKMARKED_ITEM:
                    mAdapter.swapData(data.getData());
                    break;
                default:
                    //do nothing
            }
        } else {
            SnackbarFactory
                    .createRetrieveErrorSnackbar(mBinding.rvItemList,
                            NewsFragment.this).show();
        }

        hideProgressBar();
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    private void refreshNews() {
        changeNewsType(mNewsType);
    }

    public void changeNewsType(String type) {
        mAdapter.clearData();
        mCurrentPage = 1;
        mNewsType = type;

        showProgressBar();
        retrieveNews();
    }

    private void nextPageNews() {
        mCurrentPage++;
        retrieveNews();
    }

    private void retrieveNews() {
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();

        switch (mNewsType) {
            case TYPE_TOP:
                //fall through
            case TYPE_BEST:
                //fall through
            case TYPE_NEW:
                //fall through
            case TYPE_SHOW:
                //fall through
            case TYPE_ASK:
                //fall through
            case TYPE_JOB:
                loaderManager.destroyLoader(LOADER_ID_BOOKMARKED_ITEM);
                loaderManager.destroyLoader(LOADER_ID_STORIES);
                loaderManager.initLoader(LOADER_ID_STORIES, null, getStoriesCallback());

                break;
            case TYPE_BOOKMARKED:
                loaderManager.destroyLoader(LOADER_ID_STORIES);
                loaderManager.restartLoader(LOADER_ID_BOOKMARKED_ITEM, null, this);
                break;
            default:
                hideProgressBar();
                break;
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

    private LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Long>>> getStoriesCallback() {
        return new LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Long>>>() {
            @Override
            public Loader<HackerNewsResponse<List<Long>>> onCreateLoader(int id, Bundle args) {
                switch (id) {
                    case LOADER_ID_STORIES:
                        return new StoriesLoader(getActivity(), mNewsType);
                    default:
                }

                return null;
            }

            @Override
            public void onLoadFinished(Loader<HackerNewsResponse<List<Long>>> loader,
                                       HackerNewsResponse<List<Long>> data) {
                if (data.isSuccessful()) {
                    mItemIds = data.getData();
                    getActivity().getSupportLoaderManager()
                            .restartLoader(LOADER_ID_STORIES_ITEM, null, NewsFragment.this);
                } else {
                    SnackbarFactory
                            .createRetrieveErrorSnackbar(mBinding.rvItemList,
                                    NewsFragment.this).show();

                    hideProgressBar();
                }
            }

            @Override
            public void onLoaderReset(Loader<HackerNewsResponse<List<Long>>> loader) {

            }
        };
    }
}
