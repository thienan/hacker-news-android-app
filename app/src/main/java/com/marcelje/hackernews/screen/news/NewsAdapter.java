package com.marcelje.hackernews.screen.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.BaseItemActivity;

import java.util.ArrayList;
import java.util.List;

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemViewHolder> {

    private final ToolbarActivity mActivity;
    private final List<Item> mData;

    public NewsAdapter(ToolbarActivity activity) {
        mActivity = activity;
        mData = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNewsBinding binding = ItemNewsBinding.inflate(inflater, parent, false);
        binding.setItemUserClickHandlers(new ItemUserClickHandlers(mActivity));
        binding.setItemTextClickHandlers(new ItemTextClickHandlers(mActivity));
        binding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(mActivity));

        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.binding.setItem(item);

        if (HackerNewsDao.isItemAvailable(mActivity, item.getId())) {
            holder.binding.ibBookmark.setImageResource(R.drawable.ic_bookmark);
        } else {
            holder.binding.ibBookmark.setImageResource(R.drawable.ic_bookmark_border);
        }
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

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(List<Item> data) {
        mData.addAll(data);
        notifyItemRangeInserted(mData.size() - data.size(), data.size());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ItemNewsBinding binding;

        public ItemViewHolder(final ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Item data = mData.get(getAdapterPosition());
            BaseItemActivity.startActivity(mActivity, data);
        }
    }
}