package com.marcelje.hackernews.screen.news.item;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.ItemPollOptionBinding;
import com.marcelje.hackernews.model.Item;

import java.util.ArrayList;
import java.util.List;

public class PollOptionAdapter extends RecyclerView.Adapter<PollOptionAdapter.PollOptionViewHolder> {
    private final List<Item> mData;

    public PollOptionAdapter() {
        mData = new ArrayList<>();
    }

    @Override
    public PollOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPollOptionBinding binding = ItemPollOptionBinding.inflate(inflater, parent, false);

        return new PollOptionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PollOptionViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<Item> getData() {
        return mData;
    }

    public void swapData(List<Item> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    class PollOptionViewHolder extends RecyclerView.ViewHolder {
        final ItemPollOptionBinding binding;

        public PollOptionViewHolder(ItemPollOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}