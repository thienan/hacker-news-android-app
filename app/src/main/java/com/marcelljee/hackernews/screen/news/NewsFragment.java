package com.marcelljee.hackernews.screen.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.event.ItemUpdateEvent;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.databinding.FragmentNewsBinding;
import com.marcelljee.hackernews.factory.SnackbarFactory;
import com.marcelljee.hackernews.loader.BookmarkedItemLoader;
import com.marcelljee.hackernews.loader.AppResponse;
import com.marcelljee.hackernews.loader.HistoryLoader;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.loader.StoriesLoader;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.CollectionUtils;
import com.marcelljee.hackernews.utils.SettingsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class NewsFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String STATE_NEWS_TYPE = "com.marcelljee.hackernews.screen.news.state.NEWS_TYPE";
    private static final String STATE_NEWS_DATA_IDS = "com.marcelljee.hackernews.screen.news.state.NEWS_DATA_IDS";
    private static final String STATE_CURRENT_PAGE = "com.marcelljee.hackernews.screen.news.state.CURRENT_PAGE";

    private static final int LOADER_ID_STORIES_TOP = 6100;
    private static final int LOADER_ID_STORIES_BEST = 6200;
    private static final int LOADER_ID_STORIES_NEW = 6300;
    private static final int LOADER_ID_STORIES_SHOW = 6400;
    private static final int LOADER_ID_STORIES_ASK = 6500;
    private static final int LOADER_ID_STORIES_JOB = 6600;
    private static final int LOADER_ID_HISTORY = 6700;

    private static final int LOADER_ID_STORIES_TOP_ITEM = 7100;
    private static final int LOADER_ID_STORIES_BEST_ITEM = 7200;
    private static final int LOADER_ID_STORIES_NEW_ITEM = 7300;
    private static final int LOADER_ID_STORIES_SHOW_ITEM = 7400;
    private static final int LOADER_ID_STORIES_ASK_ITEM = 7500;
    private static final int LOADER_ID_STORIES_JOB_ITEM = 7600;
    private static final int LOADER_ID_HISTORY_ITEM = 7700;
    private static final int LOADER_ID_BOOKMARKED_ITEM = 7800;

    private static final int ITEM_COUNT = 10;

    private String mNewsType;
    private List<Long> mItemIds;
    private int mCurrentPage = 1;
    private ItemAdapter mNewsAdapter;

    private FragmentNewsBinding mBinding;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mNewsAdapter = new ItemAdapter(getToolbarActivity());

        mBinding.rvItemList.setLayoutManager(layoutManager);
        mBinding.rvItemList.setAdapter(mNewsAdapter);
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
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        mNewsAdapter.closeActionModeMenu();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mNewsType = inState.getString(STATE_NEWS_TYPE);
        mItemIds = CollectionUtils.toLongList(inState.getLongArray(STATE_NEWS_DATA_IDS));
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
        mNewsAdapter.restoreState(inState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_NEWS_TYPE, mNewsType);
        outState.putLongArray(STATE_NEWS_DATA_IDS, CollectionUtils.toLongArray(mItemIds));
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        mNewsAdapter.saveState(outState);
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
        AppResponse response = (AppResponse) data;

        if (response.isSuccessful()) {
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
                        mNewsAdapter.addItems((List<Item>) response.getData());
                    }
                    break;
                case LOADER_ID_STORIES_BEST_ITEM:
                    if (getString(R.string.settings_type_option_best).equals(mNewsType)) {
                        mNewsAdapter.addItems((List<Item>) response.getData());
                    }
                    break;
                case LOADER_ID_STORIES_NEW_ITEM:
                    if (getString(R.string.settings_type_option_new).equals(mNewsType)) {
                        mNewsAdapter.addItems((List<Item>) response.getData());
                    }
                    break;
                case LOADER_ID_STORIES_SHOW_ITEM:
                    if (getString(R.string.settings_type_option_show).equals(mNewsType)) {
                        mNewsAdapter.addItems((List<Item>) response.getData());
                    }
                    break;
                case LOADER_ID_STORIES_ASK_ITEM:
                    if (getString(R.string.settings_type_option_ask).equals(mNewsType)) {
                        mNewsAdapter.addItems((List<Item>) response.getData());
                    }
                    break;
                case LOADER_ID_STORIES_JOB_ITEM:
                    if (getString(R.string.settings_type_option_jobs).equals(mNewsType)) {
                        mNewsAdapter.addItems((List<Item>) response.getData());
                    }
                    break;
                case LOADER_ID_HISTORY_ITEM:
                    if (getString(R.string.settings_type_option_history).equals(mNewsType)) {
                        mNewsAdapter.swapItems((List<Item>) response.getData());
                    }
                    break;
                case LOADER_ID_BOOKMARKED_ITEM:
                    if (getString(R.string.settings_type_option_bookmarked).equals(mNewsType)) {
                        mNewsAdapter.swapItems((List<Item>) response.getData());
                    }
                    break;
                default:
                    //do nothing
            }
        } else {
            SnackbarFactory.createRetrieveErrorSnackbar(
                    mBinding.rvItemList, (v) -> refreshNews()).show();
        }

        mBinding.rvItemList.hideProgressBar();
        mBinding.srlRefresh.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.settings_read_indicator_key).equals(key)
                && !SettingsUtils.readIndicatorEnabled(getContext())) {
            mNewsAdapter.clearReadIndicator();
        }
    }

    @Subscribe()
    @SuppressWarnings({"unused"})
    public void onItemUpdateEvent(ItemUpdateEvent event) {
        mNewsAdapter.updateItem(event.getItem());
    }

    public void showAllItems() {
        mNewsAdapter.showAll();
    }

    public void showUnreadItems() {
        mNewsAdapter.showUnread();
    }

    public void clearItems() {
        mNewsAdapter.clearItems();
        mCurrentPage = 1;
        mBinding.rvItemList.restartOnLoadMoreListener();
    }

    public void refresh() {
        mBinding.srlRefresh.setRefreshing(true);
        refreshNews();
    }

    private void refreshNews() {
        changeNewsType(mNewsType);
    }

    public void changeNewsType(String type) {
        clearItems();
        mNewsType = type;
        mBinding.rvItemList.showProgressBar();

        int loaderId;

        if (getString(R.string.settings_type_option_top).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_TOP;
        } else if (getString(R.string.settings_type_option_best).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_BEST;
        } else if (getString(R.string.settings_type_option_new).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_NEW;
        } else if (getString(R.string.settings_type_option_show).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_SHOW;
        } else if (getString(R.string.settings_type_option_ask).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_ASK;
        } else if (getString(R.string.settings_type_option_jobs).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_JOB;
        } else if (getString(R.string.settings_type_option_history).equals(mNewsType)) {
            loaderId = LOADER_ID_HISTORY;
        } else if (getString(R.string.settings_type_option_bookmarked).equals(mNewsType)) {
            loaderId = LOADER_ID_BOOKMARKED_ITEM;
        } else {
            mBinding.rvItemList.hideProgressBar();
            mBinding.srlRefresh.setRefreshing(false);
            return;
        }

        getActivity().getSupportLoaderManager().restartLoader(loaderId, null, this);
    }

    private void nextPageNews() {
        mCurrentPage++;

        int loaderId;

        if (getString(R.string.settings_type_option_top).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_TOP_ITEM;
        } else if (getString(R.string.settings_type_option_best).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_BEST_ITEM;
        } else if (getString(R.string.settings_type_option_new).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_NEW_ITEM;
        } else if (getString(R.string.settings_type_option_show).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_SHOW_ITEM;
        } else if (getString(R.string.settings_type_option_ask).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_ASK_ITEM;
        } else if (getString(R.string.settings_type_option_jobs).equals(mNewsType)) {
            loaderId = LOADER_ID_STORIES_JOB_ITEM;
        } else if (getString(R.string.settings_type_option_history).equals(mNewsType)) {
            loaderId = LOADER_ID_HISTORY_ITEM;
        } else {
            mBinding.rvItemList.hideProgressBar();
            mBinding.srlRefresh.setRefreshing(false);
            return;
        }

        getActivity().getSupportLoaderManager().restartLoader(loaderId, null, this);
    }
}
