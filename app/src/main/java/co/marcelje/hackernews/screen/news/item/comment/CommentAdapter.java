package co.marcelje.hackernews.screen.news.item.comment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.adapter.ItemAdapter;
import co.marcelje.hackernews.databinding.ItemCommentBinding;
import co.marcelje.hackernews.handlers.ItemUserClickHandlers;

class CommentAdapter extends ItemAdapter {

    public CommentAdapter(ToolbarActivity activity, String parent, String poster) {
        super(activity, parent, poster);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding binding = ItemCommentBinding.inflate(inflater, parent, false);
        binding.setActivity(getActivity());
        binding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));

        binding.tvText.setMovementMethod(LinkMovementMethod.getInstance());

        return new ItemViewHolder(binding, true);
    }
}