package com.marcelje.hackernews.screen.news;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.adapter.BaseAdapter;
import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.BaseItemActivity;

class NewsAdapter extends BaseAdapter implements BaseAdapter.OnClickListener {

    public NewsAdapter(ToolbarActivity activity) {
        super(activity);
    }

    @Override
    public BaseAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNewsBinding binding = ItemNewsBinding.inflate(inflater, parent, false);
        binding.setActivity(getActivity());
        binding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));
        binding.setItemTextClickHandlers(new ItemTextClickHandlers(getActivity()));
        binding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(getActivity()));

        return new BaseAdapter.BaseViewHolder(binding, this);
    }

    @Override
    public void onClick(int pos) {
        Item data = getData().get(pos);
        BaseItemActivity.startActivity(getActivity(), data);
    }
}