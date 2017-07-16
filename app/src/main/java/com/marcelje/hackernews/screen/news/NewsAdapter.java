package com.marcelje.hackernews.screen.news;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.details.NewsDetailsActivity;
import com.marcelje.hackernews.utils.MenuUtils;

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

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
        final ItemNewsBinding binding;

        public ItemViewHolder(ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.layoutUser.inflateMenu(R.menu.menu_item);
            binding.layoutUser.setOnMenuItemClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Item data = mData.get(getAdapterPosition());
            NewsDetailsActivity.startActivity(mActivity, data);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_share:
                    Item data = mData.get(getAdapterPosition());
                    MenuUtils.openShareHackerNewsLinkChooser(mActivity, data);
                    return true;
                default:
            }

            return false;
        }
    }
}