package com.marcelljee.hackernews.screen.news.item.head;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.chrome.CustomTabsHelper;
import com.marcelljee.hackernews.databinding.FragmentStoryBinding;
import com.marcelljee.hackernews.event.ItemRefreshEvent;
import com.marcelljee.hackernews.event.ItemUpdateEvent;
import com.marcelljee.hackernews.loader.HackerNewsResponse;
import com.marcelljee.hackernews.loader.ItemListLoader;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.databinding.viewmodel.ItemViewModel;
import com.marcelljee.hackernews.databinding.viewmodel.SectionNewsDetailsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

public class StoryFragment extends ItemHeadFragment
        implements LoaderManager.LoaderCallbacks<HackerNewsResponse<List<Item>>> {

    private static final String ARG_ITEM = "com.marcelljee.hackernews.screen.news.item.head.arg.ITEM";

    private static final int LOADER_ID_STORIES_ITEM = 200;
    private static final int LOADER_ID_POLL_OPTIONS = 250;

    private Item mItem;

    private ItemAdapter mPollOptionsAdapter;

    private FragmentStoryBinding mBinding;
    private CustomTabsHelper customTabsHelper;

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

        customTabsHelper = new CustomTabsHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentStoryBinding.inflate(inflater, container, false);
        mBinding.setItem(mItem);
        mBinding.sectionNews.itemNews.setViewModel(
                new ItemViewModel(getToolbarActivity(), false, customTabsHelper.getSession()));
        mBinding.sectionNewsDetails.setViewModel(new SectionNewsDetailsViewModel(getToolbarActivity()));

        if (TextUtils.isEmpty(mItem.getUrl())) {
            mBinding.sectionNews.itemNews.tvText.setBackground(null);
        }

        mPollOptionsAdapter = new ItemAdapter(getToolbarActivity());

        mBinding.sectionPollOptions.rvPollOptionList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.sectionPollOptions.rvPollOptionList.setAdapter(mPollOptionsAdapter);
        mBinding.sectionPollOptions.rvPollOptionList.showDivider();

        mBinding.sectionPollOptions.getRoot().setVisibility(View.GONE);

        if (savedInstanceState == null) {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_POLL_OPTIONS, null, this);
        } else {
            onRestoreInstanceState(savedInstanceState);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        customTabsHelper.bindCustomTabsService(getActivity());

        if (!TextUtils.isEmpty(mItem.getUrl())) {
            customTabsHelper.mayLaunchUrl(Uri.parse(mItem.getUrl()), null, null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        customTabsHelper.unbindCustomTabsService(getActivity());
    }

    private void onRestoreInstanceState(Bundle inState) {
        mPollOptionsAdapter.restoreState(inState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPollOptionsAdapter.saveState(outState);
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
                return new ItemListLoader(getContext(), Collections.singletonList(mItem.getId()));
            case LOADER_ID_POLL_OPTIONS:
                return new ItemListLoader(getContext(), mItem.getParts());
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
                    mItem.update(response.getData().get(0));

                    EventBus.getDefault().post(new ItemRefreshEvent(mItem));
                    EventBus.getDefault().post(new ItemUpdateEvent(mItem));
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_POLL_OPTIONS, null, this);
                    break;
                case LOADER_ID_POLL_OPTIONS:
                    if (response.getData().size() > 0) {
                        mPollOptionsAdapter.swapItems(response.getData());
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
