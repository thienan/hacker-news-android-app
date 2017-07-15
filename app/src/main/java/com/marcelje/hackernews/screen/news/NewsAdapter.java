package com.marcelje.hackernews.screen.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.details.NewsDetailsActivity;
import com.marcelje.hackernews.screen.user.UserActivity;
import com.marcelje.hackernews.utils.BrowserUtils;
import com.marcelje.hackernews.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemViewHolder> {

    private final Context mContext;
    private final List<Item> mData;

    public NewsAdapter(Context context) {
        mContext = context;
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

            binding.layoutUser.inflateMenu(R.menu.menu_news_item);

            binding.layoutUser.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_share:
                            Item data = mData.get(getAdapterPosition());
                            MenuUtils.openShareChooser(mContext, data);
                            return true;
                    }

                    return false;
                }
            });

            binding.layoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item data = mData.get(getAdapterPosition());
                    UserActivity.startActivity(mContext, data.getBy());
                }
            });

            binding.tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item data = mData.get(getAdapterPosition());

                    if (TextUtils.isEmpty(data.getUrl())) {
                        NewsDetailsActivity.startActivity(mContext, data);
                    } else {
                        BrowserUtils.openTab(mContext, data.getUrl());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item data = mData.get(getAdapterPosition());
                    NewsDetailsActivity.startActivity(mContext, data);
                }
            });
        }
    }
}