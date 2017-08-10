package com.marcelljee.hackernews.screen.news;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.databinding.ItemNewsBinding;
import com.marcelljee.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelljee.hackernews.handlers.ItemTextClickHandlers;
import com.marcelljee.hackernews.handlers.ItemUserClickHandlers;
import com.marcelljee.hackernews.menu.ActionModeMenu;

class NewsAdapter extends ItemAdapter {

    private final ActionModeMenu actionModeMenu;

    public NewsAdapter(ToolbarActivity activity) {
        super(activity);
        actionModeMenu = new ActionModeMenu();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNewsBinding binding = ItemNewsBinding.inflate(inflater, parent, false);
        binding.setActivity(getActivity());
        binding.setShowReadIndicator(true);
        binding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));
        binding.setItemTextClickHandlers(new ItemTextClickHandlers(getActivity()));
        binding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(getActivity()));

        return new ItemViewHolder(binding, true);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnLongClickListener((v) -> actionModeMenu.start(this, holder));
    }
}