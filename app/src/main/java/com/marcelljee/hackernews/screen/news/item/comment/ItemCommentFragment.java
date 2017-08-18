package com.marcelljee.hackernews.screen.news.item.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.databinding.FragmentItemCommentBinding;
import com.marcelljee.hackernews.databinding.component.AppDataBindingComponent;
import com.marcelljee.hackernews.event.ItemRefreshEvent;
import com.marcelljee.hackernews.factory.SnackbarFactory;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.loader.AppResponse;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.CollectionUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

public class ItemCommentFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks<AppResponse<List<Item>>> {

    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.ITEM";
    private static final String ARG_POSTER_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.POSTER_ITEM";
    private static final String ARG_ITEM_LOADER_OFFSET = "com.marcelljee.hackernews.screen.news.item.arg.ITEM_LOADER_OFFSET";

    private static final String STATE_CURRENT_PAGE = "com.marcelljee.hackernews.screen.news.item.state.CURRENT_PAGE";

    private static final int ITEM_COUNT = 10;

    private Item mItem;
    private Item mPosterItem;
    private int mCommentItemLoaderId = 1000;

    private int mCurrentPage = 1;
    private ItemAdapter mCommentAdapter;

    private FragmentItemCommentBinding mBinding;

    public static ItemCommentFragment newInstance(Item item, Item posterItem, int loaderOffset) {
        ItemCommentFragment fragment = new ItemCommentFragment();

        Bundle args = createArguments(item, posterItem, loaderOffset);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        extractArguments();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentItemCommentBinding.inflate(inflater, container, false,
                new AppDataBindingComponent(getToolbarActivity()));
        mBinding.setItem(mItem);

        if (mPosterItem == null) {
            mCommentAdapter = new ItemAdapter(getToolbarActivity(), null, mItem);
        } else {
            mCommentAdapter = new ItemAdapter(getToolbarActivity(), mItem, mPosterItem);
        }

        mBinding.rvCommentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvCommentList.setAdapter(mCommentAdapter);
        mBinding.rvCommentList.showDivider();
        mBinding.rvCommentList.setOnLoadMoreListener((page, totalItemsCount) -> nextPageComments());

        if (savedInstanceState == null) {
            refreshComments();
        } else {
            onRestoreInstanceState(savedInstanceState);
        }

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
        mCommentAdapter.restoreState(inState);

        if (mCommentAdapter.getItemCount() <= 0) refreshComments();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        mCommentAdapter.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<AppResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        if (id == mCommentItemLoaderId) {
            List<Long> kids = mItem.getKids();

            List<Long> list = CollectionUtils.subList(kids,
                    (mCurrentPage - 1) * ITEM_COUNT,
                    mCurrentPage * ITEM_COUNT);

            return new ItemListLoader(getContext(), list);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<AppResponse<List<Item>>> loader,
                               AppResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            if (loader.getId() == mCommentItemLoaderId) {
                mCommentAdapter.addItems(response.getData());
                mBinding.rvCommentList.hideProgressBar();
            }
        } else {
            SnackbarFactory.createRetrieveErrorSnackbar(mBinding.getRoot(), v -> retrieveComments()).show();
            mBinding.rvCommentList.hideProgressBar();
        }

    }

    @Override
    public void onLoaderReset(Loader<AppResponse<List<Item>>> loader) {

    }

    @Subscribe
    @SuppressWarnings({"unused"})
    public void onItemRefreshEvent(ItemRefreshEvent event) {
        mItem.update(event.getItem());
        refreshComments();
    }

    private void refreshComments() {
        mCommentAdapter.clearItems();
        mCurrentPage = 1;
        mBinding.rvCommentList.restartOnLoadMoreListener();
        mBinding.rvCommentList.showProgressBar();
        retrieveComments();
    }

    private void nextPageComments() {
        mCurrentPage++;
        retrieveComments();
    }

    private void retrieveComments() {
        if (mItem.getKids() == null) return;
        getActivity().getSupportLoaderManager().restartLoader(mCommentItemLoaderId, null, this);
    }

    private static Bundle createArguments(Item item, Item posterItem, int loaderOffset) {
        Bundle args = new Bundle();
        if (item != null) args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        if (posterItem != null) args.putParcelable(ARG_POSTER_ITEM, Parcels.wrap(posterItem));
        if (loaderOffset >= 0) args.putInt(ARG_ITEM_LOADER_OFFSET, loaderOffset);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_ITEM)) {
            mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        }

        if (args.containsKey(ARG_POSTER_ITEM)) {
            mPosterItem = Parcels.unwrap(args.getParcelable(ARG_POSTER_ITEM));
        }

        if (args.containsKey(ARG_ITEM_LOADER_OFFSET)) {
            mCommentItemLoaderId += args.getInt(ARG_ITEM_LOADER_OFFSET);
        }
    }
}
