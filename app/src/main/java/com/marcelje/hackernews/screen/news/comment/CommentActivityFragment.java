package com.marcelje.hackernews.screen.news.comment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.databinding.FragmentCommentBinding;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;

import org.parceler.Parcels;

public class CommentActivityFragment extends Fragment {

    private static final String ARG_ITEM = "com.marcelje.hackernews.screen.news.comment.arg.ITEM";
    private static final String ARG_PARENT = "com.marcelje.hackernews.screen.news.comment.arg.PARENT";
    private static final String ARG_POSTER = "com.marcelje.hackernews.screen.news.comment.arg.POSTER";

    private FragmentCommentBinding mBinding;
    private CommentAdapter mAdapter;

    private Item mItem;
    private String mParent;
    private String mPoster;

    public static CommentActivityFragment newInstance(Item item, String parent, String poster) {
        CommentActivityFragment fragment = new CommentActivityFragment();

        Bundle args = new Bundle();
        if (item != null) args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) args.putString(ARG_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) args.putString(ARG_POSTER, poster);

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

        if (args.containsKey(ARG_PARENT)) {
            mParent = args.getString(ARG_PARENT);
        }

        if (args.containsKey(ARG_POSTER)) {
            mPoster = args.getString(ARG_POSTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCommentBinding.inflate(inflater, container, false);
        mBinding.setItem(mItem);
        mBinding.setActivity(getActivity());
        mBinding.setParent(mParent);
        mBinding.setPoster(mPoster);
        mBinding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));

        mBinding.tvCommentInfo.setMovementMethod(LinkMovementMethod.getInstance());

        // TODO: find a better way to remove maxLines
        mBinding.sectionCommentMain.tvText.setMaxLines(Integer.MAX_VALUE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CommentAdapter(getActivity(), mItem.getBy(), mPoster);

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
        if (mItem.getKids() == null) return;

        showProgressBar();

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
