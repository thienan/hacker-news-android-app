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
import com.marcelljee.hackernews.utils.PagingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

public class ItemCommentFragment extends ToolbarFragment
        implements LoaderManager.LoaderCallbacks<AppResponse<List<Item>>> {

    private static final String ARG_ITEM_LOADER_OFFSET = "com.marcelljee.hackernews.screen.news.item.arg.ITEM_LOADER_OFFSET";
    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.ITEM";
    private static final String ARG_POSTER_ITEM = "com.marcelljee.hackernews.screen.news.item.arg.POSTER_ITEM";

    private static final String STATE_ITEM = "com.marcelljee.hackernews.screen.news.item.state.ITEM";
    private static final String STATE_CURRENT_PAGE = "com.marcelljee.hackernews.screen.news.item.state.CURRENT_PAGE";

    private int mCommentItemLoaderId = 1000;

    private Item mItem;
    private int mCurrentPage = 1;
    private ItemAdapter mCommentAdapter;

    private FragmentItemCommentBinding mBinding;

    public static ItemCommentFragment newInstance(int loaderOffset, Item item, Item posterItem) {
        ItemCommentFragment fragment = new ItemCommentFragment();

        Bundle args = createArguments(loaderOffset, item, posterItem);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        mCommentItemLoaderId += args.getInt(ARG_ITEM_LOADER_OFFSET);
        mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        Item posterItem = Parcels.unwrap(args.getParcelable(ARG_POSTER_ITEM));

        if (posterItem == null) {
            mCommentAdapter = new ItemAdapter(getToolbarActivity(), null, mItem);
        } else {
            mCommentAdapter = new ItemAdapter(getToolbarActivity(), mItem, posterItem);
        }

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
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

        mBinding.rvCommentList.setAdapter(mCommentAdapter);
        mBinding.rvCommentList.showDivider();
        mBinding.rvCommentList.setOnLoadMoreListener((page, totalItemsCount) -> nextPageComments());

        if (savedInstanceState == null) {
            refreshComments();
        }

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        if (mItem != null) {
            mItem.update(Parcels.unwrap(inState.getParcelable(STATE_ITEM)));
        } else {
            mItem = Parcels.unwrap(inState.getParcelable(STATE_ITEM));
        }

        mCurrentPage = inState.getInt(STATE_CURRENT_PAGE);
        mCommentAdapter.restoreState(inState);

        if (mCommentAdapter.getItemCount() <= 0) refreshComments();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_ITEM, Parcels.wrap(mItem));
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        mCommentAdapter.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<AppResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        if (id == mCommentItemLoaderId) {
            List<Long> items = PagingUtils.getItems(mItem.getKids(), mCurrentPage);
            return new ItemListLoader(getContext(), items);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<AppResponse<List<Item>>> loader,
                               AppResponse<List<Item>> response) {
        if (response != null && response.isSuccessful()) {
            if (loader.getId() == mCommentItemLoaderId) {
                mCommentAdapter.addItems(response.getData());
                mBinding.rvCommentList.hideProgressBar();
            }
        } else {
            if (loader.getId() == mCommentItemLoaderId) {
                mCurrentPage--;
            }

            SnackbarFactory.createRetrieveErrorSnackbar(mBinding.getRoot()).show();
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

    private static Bundle createArguments(int loaderOffset, Item item, Item posterItem) {
        Bundle args = new Bundle();
        if (loaderOffset >= 0) args.putInt(ARG_ITEM_LOADER_OFFSET, loaderOffset);
        if (item != null) args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        if (posterItem != null) args.putParcelable(ARG_POSTER_ITEM, Parcels.wrap(posterItem));

        return args;
    }
}
