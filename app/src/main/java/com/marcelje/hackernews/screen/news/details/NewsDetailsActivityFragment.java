package com.marcelje.hackernews.screen.news.details;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.databinding.FragmentNewsDetailsBinding;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.comment.CommentAdapter;
import com.marcelje.hackernews.screen.news.details.text.DetailsTextActivity;
import com.marcelje.hackernews.screen.user.UserActivity;

import org.parceler.Parcels;

public class NewsDetailsActivityFragment extends Fragment {

    private static final String ARG_ITEM = "com.marcelje.hackernews.screen.news.details.arg.ITEM";

    private FragmentNewsDetailsBinding mBinding;
    private CommentAdapter mAdapter;

    private Item mItem;

    public static NewsDetailsActivityFragment newInstance(Item item) {
        NewsDetailsActivityFragment fragment = new NewsDetailsActivityFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, Parcels.wrap(item));

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args.containsKey(ARG_ITEM)) {
            mItem = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewsDetailsBinding.inflate(inflater, container, false);
        mBinding.setItem(mItem);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CommentAdapter(getContext(), null, mItem.getBy());

        mBinding.sectionNews.tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserActivity.startActivity(getContext(), mItem.getBy());
            }
        });

        mBinding.sectionNewsDetails.tvNewsDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsTextActivity.startActivity(getContext(), mItem.getText());
            }
        });

        mBinding.sectionCommentList.rvCommentList.setLayoutManager(layoutManager);
        mBinding.sectionCommentList.rvCommentList.setAdapter(mAdapter);
        mBinding.sectionCommentList.rvCommentList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        if (savedInstanceState == null) {
            retrieveComments();
        }

        return mBinding.getRoot();
    }

    private void retrieveComments() {
        showProgressBar();

        if (mItem.getKids() == null) return;

        for (long itemId : mItem.getKids()) {
            HackerNewsApi.with(getActivity()).getItem(itemId, new HackerNewsApi.RestCallback<Item>() {
                @Override
                public void onSuccess(Item data) {
                    hideProgressBar();
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
        mBinding.sectionCommentList.pbLoading.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.sectionCommentList.pbLoading.setVisibility(View.GONE);
    }
}
