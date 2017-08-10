package com.marcelljee.hackernews.screen.user;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.databinding.ItemCommentBinding;
import com.marcelljee.hackernews.databinding.ItemNewsBinding;
import com.marcelljee.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelljee.hackernews.handlers.ItemTextClickHandlers;
import com.marcelljee.hackernews.menu.ActionModeMenu;

class SubmissionAdapter extends ItemAdapter {

    private final ActionModeMenu actionModeMenu;

    public SubmissionAdapter(ToolbarActivity mActivity) {
        super(mActivity);
        actionModeMenu = new ActionModeMenu();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_NEWS:
                ItemNewsBinding newsBinding = ItemNewsBinding.inflate(inflater, parent, false);
                newsBinding.setActivity(getActivity());
                newsBinding.setShowReadIndicator(true);
                newsBinding.setItemTextClickHandlers(new ItemTextClickHandlers(getActivity()));
                newsBinding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(getActivity()));
                return new ItemViewHolder(newsBinding, true);
            case VIEW_TYPE_COMMENT:
                ItemCommentBinding commentBinding = ItemCommentBinding.inflate(inflater, parent, false);
                commentBinding.setActivity(getActivity());
                commentBinding.tvText.setMovementMethod(LinkMovementMethod.getInstance());
                return new ItemViewHolder(commentBinding, true);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnLongClickListener((v) -> actionModeMenu.start(this, holder));
    }
}