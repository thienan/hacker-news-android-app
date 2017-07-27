package com.marcelje.hackernews.screen.user;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.adapter.ItemAdapter;
import com.marcelje.hackernews.databinding.ItemCommentBinding;
import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;

class SubmissionAdapter extends ItemAdapter {

    private static final int VIEW_TYPE_NEWS = 1;
    private static final int VIEW_TYPE_COMMENT = 2;

    private static final String ITEM_TYPE_COMMENT = "comment";
    private static final String ITEM_TYPE_STORY = "story";
    private static final String ITEM_TYPE_POLL = "poll";
    private static final String ITEM_TYPE_JOB = "job";

    public SubmissionAdapter(ToolbarActivity mActivity) {
        super(mActivity);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_NEWS:
                ItemNewsBinding newsBinding = ItemNewsBinding.inflate(inflater, parent, false);
                newsBinding.setActivity(getActivity());
                newsBinding.setItemTextClickHandlers(new ItemTextClickHandlers(getActivity()));
                newsBinding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(getActivity()));
                return new ItemViewHolder(newsBinding, true);
            case VIEW_TYPE_COMMENT:
                ItemCommentBinding commentBinding = ItemCommentBinding.inflate(inflater, parent, false);
                commentBinding.tvText.setMovementMethod(LinkMovementMethod.getInstance());
                return new ItemViewHolder(commentBinding, true);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (getData().get(position).getType()) {
            case ITEM_TYPE_COMMENT:
                return VIEW_TYPE_COMMENT;
            case ITEM_TYPE_STORY:
                //fall through
            case ITEM_TYPE_JOB:
                //fall through
            case ITEM_TYPE_POLL:
                //fall through
            default:
                return VIEW_TYPE_NEWS;
        }
    }
}