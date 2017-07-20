package com.marcelje.hackernews.screen.news;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.database.HackerNewsDao;
import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.ItemActivity;
import com.marcelje.hackernews.utils.BrowserUtils;
import com.marcelje.hackernews.utils.HackerNewsUtils;
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
        setUpMenuItem(holder.binding.layoutUser, item.getId());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(List<Item> data) {
        mData.addAll(data);
        notifyItemRangeInserted(mData.size() - data.size(), data.size());
    }

    private void setUpMenuItem(Toolbar toolbar, long itemId) {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_bookmark);

        if (HackerNewsDao.isItemAvailable(mActivity, itemId)) {
            menuItem.setTitle(R.string.menu_item_unbookmark);
        } else {
            menuItem.setTitle(R.string.menu_item_bookmark);
        }

        toolbar.getMenu().findItem(R.id.action_refresh).setVisible(false);

        toolbar.invalidate();
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
            ItemActivity.startActivity(mActivity, data);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Item data = mData.get(getAdapterPosition());

            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    MenuUtils.openShareHackerNewsLinkChooser(mActivity, data);

                    return true;
                case R.id.action_bookmark:
                    if (HackerNewsDao.isItemAvailable(mActivity, data.getId())) {
                        HackerNewsDao.deleteItem(mActivity, data.getId());
                        SnackbarFactory.createUnbookmarkedSuccessSnackBar(binding.tvText).show();
                        menuItem.setTitle(R.string.menu_item_bookmark);
                    } else {
                        HackerNewsDao.insertItem(mActivity, data);
                        SnackbarFactory.createBookmarkedSuccessSnackBar(binding.tvText).show();
                        menuItem.setTitle(R.string.menu_item_unbookmark);
                    }

                    return true;
                case R.id.action_open_page:
                    BrowserUtils.openTab(mActivity, HackerNewsUtils.geItemUrl(data.getId()));

                    return true;
                default:
            }

            return false;
        }
    }
}