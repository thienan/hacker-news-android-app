package co.marcelje.hackernews.screen.news.item.head;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.adapter.ItemAdapter;
import co.marcelje.hackernews.databinding.ItemPollOptionBinding;

class PollOptionAdapter extends ItemAdapter {

    public PollOptionAdapter(ToolbarActivity mActivity) {
        super(mActivity);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPollOptionBinding binding = ItemPollOptionBinding.inflate(inflater, parent, false);

        return new ItemViewHolder(binding, false);
    }
}