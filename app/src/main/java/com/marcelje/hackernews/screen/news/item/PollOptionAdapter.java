package com.marcelje.hackernews.screen.news.item;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.BR;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.adapter.BaseAdapter;
import com.marcelje.hackernews.databinding.ItemPollOptionBinding;
import com.marcelje.hackernews.model.Item;

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

    @Override
    public void onBindViewHolder(BaseAdapter.BaseViewHolder holder, int position) {
        Item item = getData().get(position);
        holder.binding.setVariable(BR.item, item);
    }
}