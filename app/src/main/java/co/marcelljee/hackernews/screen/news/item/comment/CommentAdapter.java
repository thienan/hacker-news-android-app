package com.marcelljee.hackernews.screen.news.item.comment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.databinding.ItemCommentBinding;
import com.marcelljee.hackernews.handlers.ItemUserClickHandlers;

class CommentAdapter extends ItemAdapter {

    public CommentAdapter(ToolbarActivity activity, String itemParentName, String itemPosterName) {
        super(activity, itemParentName, itemPosterName);
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