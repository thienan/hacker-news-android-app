package com.marcelljee.hackernews.screen.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.event.ItemBookmarkEvent;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.databinding.FragmentNewsBinding;
import com.marcelljee.hackernews.factory.SnackbarFactory;
import com.marcelljee.hackernews.loader.BookmarkedItemLoader;
import com.marcelljee.hackernews.loader.HackerNewsResponse;
import com.marcelljee.hackernews.loader.HistoryLoader;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.loader.StoriesLoader;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.CollectionUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.util.List;

public class NewsFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks {

    private static final String STATE_NEWS_TYPE = "com.marcelljee.hackernews.screen.news.state.NEWS_TYPE";
    private static final String STATE_NEWS_DATA_IDS = "com.marcelljee.hackernews.screen.news.state.NEWS_DATA_IDS";
    private static final String STATE_NEWS_DATA = "com.marcelljee.hackernews.screen.news.state.NEWS_DATA";
    private static final String STATE_NEWS_VIEW = "com.marcelljee.hackernews.screen.news.state.NEWS_VIEW";
    private static final String STATE_CURRENT_PAGE = "com.marcelljee.hackernews.screen.news.state.CURRENT_PAGE";

    private static final int LOADER_ID_STORIES_TOP = 510;
    private static final int LOADER_ID_STORIES_BEST = 520;
    private static final int LOADER_ID_STORIES_NEW = 530;
    private static final int LOADER_ID_STORIES_SHOW = 540;
    private static final int LOADER_ID_STORIES_ASK = 550;
    private static final int LOADER_ID_STORIES_JOB = 560;
    private static final int LOADER_ID_HISTORY = 570;

    private static final int LOADER_ID_STORIES_TOP_ITEM = 610;
    private static final int LOADER_ID_STORIES_BEST_ITEM = 620;
    private static final int LOADER_ID_STORIES_NEW_ITEM = 630;
    private static final int LOADER_ID_STORIES_SHOW_ITEM = 640;
    private static final int LOADER_ID_STORIES_ASK_ITEM = 650;
    private static final int LOADER_ID_STORIES_JOB_ITEM = 660;
    private static final int LOADER_ID_HISTORY_ITEM = 670;
    private static final int LOADER_ID_BOOKMARKED_ITEM = 680;

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
        mBinding.rvItemList.showDivider();
        mBinding.rvItemList.setOnLoadMoreListener((page, totalItemsCount) -> nextPageNews());

        mBinding.srlRefresh.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlRefresh.setOnRefreshListener(this::refreshNews);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mNewsType = inState.getString(STATE_NEWS_TYPE);
        mItemIds = CollectionUtils.toLongList(inState.getLongArray(STATE_NEWS_DATA_IDS));
        mAdapter.swapItems(Parcels.unwrap(inState.getParcelable(STATE_NEWS_DATA)));
        mBinding.rvItemList.getLayoutManager().onRestoreInstanceState(inState.getParcelable(STATE_NEWS_VIEW));
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_NEWS_TYPE, mNewsType);
        outState.putLongArray(STATE_NEWS_DATA_IDS, CollectionUtils.toLongArray(mItemIds));
        outState.putParcelable(STATE_NEWS_DATA, Parcels.wrap(mAdapter.getItems()));
        outState.putParcelable(STATE_NEWS_VIEW, mBinding.rvItemList.getLayoutManager().onSaveInstanceState());
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_STORIES_TOP:
            case LOADER_ID_STORIES_BEST:
            case LOADER_ID_STORIES_NEW:
            case LOADER_ID_STORIES_SHOW:
            case LOADER_ID_STORIES_ASK:
            case LOADER_ID_STORIES_JOB:
                return new StoriesLoader(getContext(), mNewsType);
            case LOADER_ID_HISTORY:
                return new HistoryLoader(getContext());
            case LOADER_ID_STORIES_TOP_ITEM:
            case LOADER_ID_STORIES_BEST_ITEM:
            case LOADER_ID_STORIES_NEW_ITEM:
            case LOADER_ID_STORIES_SHOW_ITEM:
            case LOADER_ID_STORIES_ASK_ITEM:
            case LOADER_ID_STORIES_JOB_ITEM:
            case LOADER_ID_HISTORY_ITEM:
                List<Long> list = CollectionUtils.subList(mItemIds,
                        (mCurrentPage - 1) * ITEM_COUNT,
                        mCurrentPage * ITEM_COUNT);

                return new ItemListLoader(getContext(), list);
            case LOADER_ID_BOOKMARKED_ITEM:
                return new BookmarkedItemLoader(getContext());
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader,
                               Object data) {
        HackerNewsResponse response = (HackerNewsResponse) data;

        if (!response.isSuccessful()) {
            SnackbarFactory.createRetrieveErrorSnackbar(mBinding.rvItemList, (v) -> retrieveNews()).show();
            return;
        }

        switch (loader.getId()) {
            case LOADER_ID_STORIES_TOP:
                mItemIds = (List<Long>) response.getData();
                getActivity().getSupportLoaderManager()
                        .restartLoader(LOADER_ID_STORIES_TOP_ITEM, null, this);
                return;
            case LOADER_ID_STORIES_BEST:
                mItemIds = (List<Long>) response.getData();
                getActivity().getSupportLoaderManager()
                        .restartLoader(LOADER_ID_STORIES_BEST_ITEM, null, this);
                return;
            case LOADER_ID_STORIES_NEW:
                mItemIds = (List<Long>) response.getData();
                getActivity().getSupportLoaderManager()
                        .restartLoader(LOADER_ID_STORIES_NEW_ITEM, null, this);
                return;
            case LOADER_ID_STORIES_SHOW:
                mItemIds = (List<Long>) response.getData();
                getActivity().getSupportLoaderManager()
                        .restartLoader(LOADER_ID_STORIES_SHOW_ITEM, null, this);
                return;
            case LOADER_ID_STORIES_ASK:
                mItemIds = (List<Long>) response.getData();
                getActivity().getSupportLoaderManager()
                        .restartLoader(LOADER_ID_STORIES_ASK_ITEM, null, this);
                return;
            case LOADER_ID_STORIES_JOB:
                mItemIds = (List<Long>) response.getData();
                getActivity().getSupportLoaderManager()
                        .restartLoader(LOADER_ID_STORIES_JOB_ITEM, null, this);
                return;
            case LOADER_ID_HISTORY:
                mItemIds = (List<Long>) response.getData();
                getActivity().getSupportLoaderManager()
                        .restartLoader(LOADER_ID_HISTORY_ITEM, null, this);
                return;
            case LOADER_ID_STORIES_TOP_ITEM:
                if (getString(R.string.settings_type_option_top).equals(mNewsType)) {
                    mAdapter.addItems((List<Item>) response.getData());
                }
                break;
            case LOADER_ID_STORIES_BEST_ITEM:
                if (getString(R.string.settings_type_option_best).equals(mNewsType)) {
                    mAdapter.addItems((List<Item>) response.getData());
                }
                break;
            case LOADER_ID_STORIES_NEW_ITEM:
                if (getString(R.string.settings_type_option_new).equals(mNewsType)) {
                    mAdapter.addItems((List<Item>) response.getData());
                }
                break;
            case LOADER_ID_STORIES_SHOW_ITEM:
                if (getString(R.string.settings_type_option_show).equals(mNewsType)) {
                    mAdapter.addItems((List<Item>) response.getData());
                }
                break;
            case LOADER_ID_STORIES_ASK_ITEM:
                if (getString(R.string.settings_type_option_ask).equals(mNewsType)) {
                    mAdapter.addItems((List<Item>) response.getData());
                }
                break;
            case LOADER_ID_STORIES_JOB_ITEM:
                if (getString(R.string.settings_type_option_jobs).equals(mNewsType)) {
                    mAdapter.addItems((List<Item>) response.getData());
                }
                break;
            case LOADER_ID_HISTORY_ITEM:
                if (getString(R.string.settings_type_option_history).equals(mNewsType)) {
                    mAdapter.swapItems((List<Item>) response.getData());
                }
                break;
            case LOADER_ID_BOOKMARKED_ITEM:
                if (getString(R.string.settings_type_option_bookmarked).equals(mNewsType)) {
                    mAdapter.swapItems((List<Item>) response.getData());
                }
                break;
            default:
                //do nothing
        }

        mBinding.rvItemList.hideProgressBar();
        mBinding.srlRefresh.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings({"UnusedParameters", "unused"})
    public void onBookmarkEvent(ItemBookmarkEvent.StoryActivityEvent event) {
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings({"UnusedParameters", "unused"})
    public void onBookmarkEvent(ItemBookmarkEvent.UserActivityEvent event) {
        mAdapter.notifyDataSetChanged();
    }

    public void refresh() {
        mBinding.srlRefresh.setRefreshing(true);
        refreshNews();
    }

    private void refreshNews() {
        changeNewsType(mNewsType);
    }

    public void changeNewsType(String type) {
        mAdapter.clearItems();
        mCurrentPage = 1;
        mNewsType = type;

        mBinding.rvItemList.showProgressBar();
        retrieveNews();
    }

    private void nextPageNews() {
        mCurrentPage++;
        retrieveNews();
    }

    private void retrieveNews() {
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();

        if (getString(R.string.settings_type_option_top).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_STORIES_TOP, null, this);
        } else if (getString(R.string.settings_type_option_best).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_STORIES_BEST, null, this);
        } else if (getString(R.string.settings_type_option_new).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_STORIES_NEW, null, this);
        } else if (getString(R.string.settings_type_option_show).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_STORIES_SHOW, null, this);
        } else if (getString(R.string.settings_type_option_ask).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_STORIES_ASK, null, this);
        } else if (getString(R.string.settings_type_option_jobs).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_STORIES_JOB, null, this);
        } else if (getString(R.string.settings_type_option_history).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_HISTORY, null, this);
        } else if (getString(R.string.settings_type_option_bookmarked).equals(mNewsType)) {
            loaderManager.restartLoader(LOADER_ID_BOOKMARKED_ITEM, null, this);
        } else {
            mBinding.rvItemList.hideProgressBar();
            mBinding.srlRefresh.setRefreshing(false);
        }
    }
}
