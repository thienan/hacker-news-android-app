package co.marcelje.hackernews.screen.news;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.adapter.ItemAdapter;
import co.marcelje.hackernews.databinding.ItemNewsBinding;
import co.marcelje.hackernews.handlers.ItemBookmarkClickHandlers;
import co.marcelje.hackernews.handlers.ItemTextClickHandlers;
import co.marcelje.hackernews.handlers.ItemUserClickHandlers;

class NewsAdapter extends ItemAdapter {

    public NewsAdapter(ToolbarActivity activity) {
        super(activity);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNewsBinding binding = ItemNewsBinding.inflate(inflater, parent, false);
        binding.setActivity(getActivity());
        binding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));
        binding.setItemTextClickHandlers(new ItemTextClickHandlers(getActivity()));
        binding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(getActivity()));

        return new ItemViewHolder(binding, true);
    }
}