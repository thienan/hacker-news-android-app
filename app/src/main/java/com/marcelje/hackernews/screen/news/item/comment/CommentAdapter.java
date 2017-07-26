package com.marcelje.hackernews.screen.news.item.comment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.adapter.BaseAdapter;
import com.marcelje.hackernews.databinding.ItemCommentBinding;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.BaseItemActivity;

public class CommentAdapter extends BaseAdapter implements BaseAdapter.OnClickListener {

    private final String mParent;
    private final String mPoster;

    public CommentAdapter(ToolbarActivity mActivity, String mParent, String mPoster) {
        super(mActivity);
        this.mParent = mParent;
        this.mPoster = mPoster;
    }

    @Override
    public BaseAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding binding = ItemCommentBinding.inflate(inflater, parent, false);
        binding.setActivity(getActivity());
        binding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));

        binding.tvText.setMovementMethod(LinkMovementMethod.getInstance());

        return new BaseAdapter.BaseViewHolder(binding, this);
    }

    @Override
    public void onClick(int pos) {
        Item data = getData().get(pos);
        BaseItemActivity.startActivity(getActivity(), data, mParent, mPoster);
    }
}