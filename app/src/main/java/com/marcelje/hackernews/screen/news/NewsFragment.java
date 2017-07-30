package com.marcelje.hackernews.screen.news;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
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

import org.parceler.Parcels;

import java.util.List;

public class NewsFragment extends ToolbarFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String STATE_NEWS_TYPE = "com.marcelje.hackernews.screen.news.state.NEWS_TYPE";
    private static final String STATE_NEWS_DATA_IDS = "com.marcelje.hackernews.screen.news.state.NEWS_DATA_IDS";
    private static final String STATE_NEWS_DATA = "com.marcelje.hackernews.screen.news.state.NEWS_DATA";
    private static final String STATE_NEWS_VIEW = "com.marcelje.hackernews.screen.news.state.NEWS_VIEW";
    private static final String STATE_CURRENT_PAGE = "com.marcelje.hackernews.screen.news.state.CURRENT_PAGE";

    private static final String TYPE_TOP = "Top";
    private static final String TYPE_BEST = "Best";
    private static final String TYPE_NEW = "New";
    private static final String TYPE_SHOW = "Show";
    private static final String TYPE_ASK = "Ask";
    private static final String TYPE_JOB = "Jobs";
    private static final String TYPE_BOOKMARKED = "Bookmarked";

    private static final int LOADER_ID_STORIES_TOP = 510;
    private static final int LOADER_ID_STORIES_BEST = 520;
    private static final int LOADER_ID_STORIES_NEW = 530;
    private static final int LOADER_ID_STORIES_SHOW = 540;
    private static final int LOADER_ID_STORIES_ASK = 550;
    private static final int LOADER_ID_STORIES_JOB = 560;

    private static final int LOADER_ID_STORIES_TOP_ITEM = 610;
    private static final int LOADER_ID_STORIES_BEST_ITEM = 620;
    private static final int LOADER_ID_STORIES_NEW_ITEM = 630;
    private static final int LOADER_ID_STORIES_SHOW_ITEM = 640;
    private static final int LOADER_ID_STORIES_ASK_ITEM = 650;
    private static final int LOADER_ID_STORIES_JOB_ITEM = 660;

    private static final int LOADER_ID_BOOKMARKED_ITEM = 700;

    private static final int ITEM_COUNT = 10;

    private String mNewsType;
    private List<Long> mItemIds;

    private FragmentNewsBinding mBinding;
    private NewsAdapter mAdapter;

    private int mCurrentPage = 1;

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
        mBinding.rvItemList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mNewsType = inState.getString(STATE_NEWS_TYPE);
        mItemIds = CollectionUtils.toLongList(inState.getLongArray(STATE_NEWS_DATA_IDS));
        mAdapter.swapData(Parcels.unwrap(inState.getParcelable(STATE_NEWS_DATA)));
        mBinding.rvItemList.getLayoutManager().onRestoreInstanceState(inState.getParcelable(STATE_NEWS_VIEW));
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_NEWS_TYPE, mNewsType);
        outState.putLongArray(STATE_NEWS_DATA_IDS, CollectionUtils.toLongArray(mItemIds));
        outState.putParcelable(STATE_NEWS_DATA, Parcels.wrap(mAdapter.getData()));
        outState.putParcelable(STATE_NEWS_VIEW, mBinding.rvItemList.getLayoutManager().onSaveInstanceState());
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        super.onSaveInstanceState(outState);
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
            case LOADER_ID_STORIES_TOP_ITEM:
                //fall through
            case LOADER_ID_STORIES_BEST_ITEM:
                //fall through
            case LOADER_ID_STORIES_NEW_ITEM:
                //fall through
            case LOADER_ID_STORIES_SHOW_ITEM:
                //fall through
            case LOADER_ID_STORIES_ASK_ITEM:
                //fall through
            case LOADER_ID_STORIES_JOB_ITEM:
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
                case LOADER_ID_STORIES_TOP_ITEM:
                    if (TYPE_TOP.equals(mNewsType)) {
                        mAdapter.addData(data.getData());
                    }
                    break;
                case LOADER_ID_STORIES_BEST_ITEM:
                    if (TYPE_BEST.equals(mNewsType)) {
                        mAdapter.addData(data.getData());
                    }
                    break;
                case LOADER_ID_STORIES_NEW_ITEM:
                    if (TYPE_NEW.equals(mNewsType)) {
                        mAdapter.addData(data.getData());
                    }
                    break;
                case LOADER_ID_STORIES_SHOW_ITEM:
                    if (TYPE_SHOW.equals(mNewsType)) {
                        mAdapter.addData(data.getData());
                    }
                    break;
                case LOADER_ID_STORIES_ASK_ITEM:
                    if (TYPE_ASK.equals(mNewsType)) {
                        mAdapter.addData(data.getData());
                    }
                    break;
                case LOADER_ID_STORIES_JOB_ITEM:
                    if (TYPE_JOB.equals(mNewsType)) {
                        mAdapter.addData(data.getData());
                    }
                    break;
                case LOADER_ID_BOOKMARKED_ITEM:
                    if (TYPE_BOOKMARKED.equals(mNewsType)) {
                        mAdapter.swapData(data.getData());
                    }
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

    public void refresh() {
        mBinding.srlRefresh.setRefreshing(true);
        refreshNews();
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
                loaderManager.restartLoader(LOADER_ID_STORIES_TOP, null, getStoriesCallback());
                break;
            case TYPE_BEST:
                loaderManager.restartLoader(LOADER_ID_STORIES_BEST, null, getStoriesCallback());
                break;
            case TYPE_NEW:
                loaderManager.restartLoader(LOADER_ID_STORIES_NEW, null, getStoriesCallback());
                break;
            case TYPE_SHOW:
                loaderManager.restartLoader(LOADER_ID_STORIES_SHOW, null, getStoriesCallback());
                break;
            case TYPE_ASK:
                loaderManager.restartLoader(LOADER_ID_STORIES_ASK, null, getStoriesCallback());
                break;
            case TYPE_JOB:
                loaderManager.restartLoader(LOADER_ID_STORIES_JOB, null, getStoriesCallback());
                break;
            case TYPE_BOOKMARKED:
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
                    case LOADER_ID_STORIES_TOP:
                        //fall through
                    case LOADER_ID_STORIES_BEST:
                        //fall through
                    case LOADER_ID_STORIES_NEW:
                        //fall through
                    case LOADER_ID_STORIES_SHOW:
                        //fall through
                    case LOADER_ID_STORIES_ASK:
                        //fall through
                    case LOADER_ID_STORIES_JOB:
                        return new StoriesLoader(getActivity(), mNewsType);
                    default:
                }

                return null;
            }

            @Override
            public void onLoadFinished(Loader<HackerNewsResponse<List<Long>>> loader,
                                       HackerNewsResponse<List<Long>> data) {
                if (data.isSuccessful()) {

                    int storiesItemId = -1;

                    switch (loader.getId()) {
                        case LOADER_ID_STORIES_TOP:
                            storiesItemId = LOADER_ID_STORIES_TOP_ITEM;
                            break;
                        case LOADER_ID_STORIES_BEST:
                            storiesItemId = LOADER_ID_STORIES_BEST_ITEM;
                            break;
                        case LOADER_ID_STORIES_NEW:
                            storiesItemId = LOADER_ID_STORIES_NEW_ITEM;
                            break;
                        case LOADER_ID_STORIES_SHOW:
                            storiesItemId = LOADER_ID_STORIES_SHOW_ITEM;
                            break;
                        case LOADER_ID_STORIES_ASK:
                            storiesItemId = LOADER_ID_STORIES_ASK_ITEM;
                            break;
                        case LOADER_ID_STORIES_JOB:
                            storiesItemId = LOADER_ID_STORIES_JOB_ITEM;
                            break;
                        default:
                    }

                    if (storiesItemId > 0) {
                        mItemIds = data.getData();
                        getActivity().getSupportLoaderManager()
                                .restartLoader(storiesItemId, null, NewsFragment.this);
                    }
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
