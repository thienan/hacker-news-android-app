package com.marcelje.hackernews.screen.news.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.adapter.BaseAdapter;
import com.marcelje.hackernews.databinding.ItemPollOptionBinding;

public class PollOptionAdapter extends BaseAdapter {

    public PollOptionAdapter(ToolbarActivity mActivity) {
        super(mActivity);
    }

    @Override
    public BaseAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPollOptionBinding binding = ItemPollOptionBinding.inflate(inflater, parent, false);

        return new BaseAdapter.BaseViewHolder(binding, null);
    }
}