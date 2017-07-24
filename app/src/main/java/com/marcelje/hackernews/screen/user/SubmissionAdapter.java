package com.marcelje.hackernews.screen.user;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcelje.hackernews.BR;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.adapter.BaseAdapter;
import com.marcelje.hackernews.databinding.ItemCommentBinding;
import com.marcelje.hackernews.databinding.ItemNewsBinding;
import com.marcelje.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.BaseItemActivity;

public class SubmissionAdapter extends BaseAdapter implements BaseAdapter.OnClickListener {

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
    public BaseAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_NEWS:
                ItemNewsBinding newsBinding = ItemNewsBinding.inflate(inflater, parent, false);
                newsBinding.setActivity(getActivity());
                newsBinding.setItemTextClickHandlers(new ItemTextClickHandlers(getActivity()));
                newsBinding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(getActivity()));
                return new BaseAdapter.BaseViewHolder(newsBinding, this);
            case VIEW_TYPE_COMMENT:
                ItemCommentBinding commentBinding = ItemCommentBinding.inflate(inflater, parent, false);
                return new BaseAdapter.BaseViewHolder(commentBinding, this);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseAdapter.BaseViewHolder holder, int position) {
        Item item = getData().get(position);
        holder.binding.setVariable(BR.item, item);
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

    @Override
    public void onClick(int pos) {
        Item data = getData().get(pos);
        BaseItemActivity.startActivity(getActivity(), data);
    }
}