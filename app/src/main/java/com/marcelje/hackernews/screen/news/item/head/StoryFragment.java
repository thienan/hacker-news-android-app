package com.marcelje.hackernews.screen.news.item.head;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.FragmentStoryBinding;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;
import com.marcelje.hackernews.handlers.ItemTextDetailsClickHandlers;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.loader.ItemListLoader;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.ItemHeadFragment;
import com.marcelje.hackernews.screen.news.item.PollOptionAdapter;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

public class StoryFragment extends ItemHeadFragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String ARG_ITEM = "com.marcelje.hackernews.screen.news.item.head.arg.ITEM";

    private static final String STATE_POLL_OPTIONS_DATA = "com.marcelje.hackernews.screen.news.item.head.state.POLL_OPTION_DATA";

    private static final int LOADER_ID_STORIES_ITEM = 200;
    private static final int LOADER_ID_POLL_OPTIONS = 250;

    private FragmentStoryBinding mBinding;

    private PollOptionAdapter mAdapter;

    private Item mItem;

    public static StoryFragment newInstance(Item item) {
        StoryFragment fragment = new StoryFragment();

        Bundle args = createArguments(item);
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
        mBinding = FragmentStoryBinding.inflate(inflater, container, false);
        mBinding.setActivity(getToolbarActivity());
        mBinding.setItem(mItem);
        mBinding.setItemUserClickHandlers(new ItemUserClickHandlers(getToolbarActivity()));
        mBinding.setItemTextClickHandlers(new ItemTextClickHandlers(getToolbarActivity()));
        mBinding.setItemTextDetailsClickHandlers(new ItemTextDetailsClickHandlers(getToolbarActivity()));

        if (TextUtils.isEmpty(mItem.getUrl())) {
            mBinding.sectionNews.tvText.setBackground(null);
        }

        mAdapter = new PollOptionAdapter();

        mBinding.sectionPollOptions.rvPollOptionList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.sectionPollOptions.rvPollOptionList.setAdapter(mAdapter);
        mBinding.sectionPollOptions.rvPollOptionList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mBinding.sectionPollOptions.getRoot().setVisibility(View.GONE);

        if (savedInstanceState == null) {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_POLL_OPTIONS, null, this);
        } else {
            onRestoreInstanceState(savedInstanceState);
        }

        return mBinding.getRoot();
    }

    private void onRestoreInstanceState(Bundle inState) {
        mAdapter.swapData((List<Item>) Parcels.unwrap(inState.getParcelable(STATE_POLL_OPTIONS_DATA)));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_POLL_OPTIONS_DATA, Parcels.wrap(mAdapter.getData()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void refresh() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_STORIES_ITEM, null, this);
    }

    @Override
    public Loader<HackerNewsResponse<List<Item>>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_STORIES_ITEM:
                return new ItemListLoader(getActivity(), Collections.singletonList(mItem.getId()));
            case LOADER_ID_POLL_OPTIONS:
                return new ItemListLoader(getActivity(), mItem.getParts());
            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<HackerNewsResponse<List<Item>>> loader,
                               HackerNewsResponse<List<Item>> response) {
        if (response.isSuccessful()) {
            switch (loader.getId()) {
                case LOADER_ID_STORIES_ITEM:
                    mItem = response.getData().get(0);
                    mBinding.setItem(mItem);

                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_POLL_OPTIONS, null, this);
                    break;
                case LOADER_ID_POLL_OPTIONS:
                    if (response.getData().size() > 0) {
                        mAdapter.swapData(response.getData());
                        mBinding.sectionPollOptions.getRoot().setVisibility(View.VISIBLE);
                    }

                    break;
                default:
                    // do nothing
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<HackerNewsResponse<List<Item>>> loader) {

    }

    private static Bundle createArguments(Item item) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, Parcels.wrap(item));

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_ITEM)) {
            mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        }
    }
}
