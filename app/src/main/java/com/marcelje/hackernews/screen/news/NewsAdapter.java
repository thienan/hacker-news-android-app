package com.marcelje.hackernews.screen.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.model.Item;

import java.util.ArrayList;
import java.util.List;

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemViewHolder> {

    private final List<Item> mData;

    public NewsAdapter() {
        mData = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNewsBinding binding = ItemNewsBinding.inflate(inflater, parent, false);

        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(Item data) {
        mData.add(data);
        notifyItemInserted(mData.size());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        final ItemNewsBinding binding;

        public ItemViewHolder(ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}