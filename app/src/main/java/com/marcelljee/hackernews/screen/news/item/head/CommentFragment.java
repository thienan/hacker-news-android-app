package com.marcelljee.hackernews.screen.news.item.head;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.databinding.FragmentCommentBinding;
import com.marcelljee.hackernews.databinding.component.AppDataBindingComponent;
import com.marcelljee.hackernews.event.ItemRefreshEvent;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.loader.AppResponse;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.databinding.viewmodel.ItemViewModel;
import com.marcelljee.hackernews.utils.CollectionUtils;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.List;

public class CommentFragment extends ItemHeadFragment
        implements LoaderManager.LoaderCallbacks<AppResponse<List<Item>>> {

    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.head.arg.ITEM";

    private static final String STATE_ITEM = "com.marcelljee.hackernews.screen.news.item.head.state.ITEM";

    private static final int LOADER_ID_COMMENT_HEAD = 2000;

    private Item mItem;

    public static CommentFragment newInstance(Item item) {
        CommentFragment fragment = new CommentFragment();

        Bundle args = createArguments(item);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCommentBinding binding = FragmentCommentBinding.inflate(inflater, container, false,
                new AppDataBindingComponent(getToolbarActivity()));
        binding.setItem(mItem);

        binding.commentHead.setViewModel(new ItemViewModel(getToolbarActivity(),
                CollectionUtils.singleItemList(mItem)));
        binding.commentHead.tvCommentText.setMaxLines(Integer.MAX_VALUE);

        return binding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        if (mItem != null) {
            mItem.update(Parcels.unwrap(inState.getParcelable(STATE_ITEM)));
        } else {
            mItem = Parcels.unwrap(inState.getParcelable(STATE_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_ITEM, Parcels.wrap(mItem));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void refresh() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_COMMENT_HEAD, null, this);
    }

    @Override
    public Loader<AppResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_COMMENT_HEAD:
                return new ItemListLoader(getContext(), CollectionUtils.singleItemList(mItem.getId()));
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<AppResponse<List<Item>>> loader,
                               AppResponse<List<Item>> response) {
        if (response != null && response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_COMMENT_HEAD:
                    mItem.update(response.getData().get(0));
                    EventBus.getDefault().post(new ItemRefreshEvent(mItem));
                    break;
                default:
                    //do nothing
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<AppResponse<List<Item>>> loader) {

    }

    private static Bundle createArguments(Item item) {
        Bundle args = new Bundle();
        if (item != null) args.putParcelable(ARG_ITEM, Parcels.wrap(item));

        return args;
    }
}
