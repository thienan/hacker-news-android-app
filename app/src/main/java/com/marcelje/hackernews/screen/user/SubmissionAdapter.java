package com.marcelje.hackernews.screen.user;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.BR;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.databinding.SubmissionCommentBinding;
import com.marcelje.hackernews.databinding.SubmissionNewsBinding;
import com.marcelje.hackernews.handlers.ItemTextClickHandlers;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.ItemActivity;

import java.util.ArrayList;
import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder> {

    private static final int VIEW_TYPE_NEWS = 1;
    private static final int VIEW_TYPE_COMMENT = 2;

    private static final String ITEM_TYPE_COMMENT = "comment";
    private static final String ITEM_TYPE_STORY = "story";
    private static final String ITEM_TYPE_POLL = "poll";
    private static final String ITEM_TYPE_JOB = "job";

    private final ToolbarActivity mActivity;
    private final List<Item> mData;

    public SubmissionAdapter(ToolbarActivity activity) {
        mActivity = activity;
        mData = new ArrayList<>();
    }

    @Override
    public SubmissionAdapter.SubmissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_NEWS:
                SubmissionNewsBinding newsBinding = SubmissionNewsBinding.inflate(inflater, parent, false);
                newsBinding.setItemTextClickHandlers(new ItemTextClickHandlers(mActivity));
                return new ItemViewHolder(newsBinding);
            case VIEW_TYPE_COMMENT:
                SubmissionCommentBinding commentBinding = SubmissionCommentBinding.inflate(inflater, parent, false);
                return new CommentViewHolder(commentBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(SubmissionAdapter.SubmissionViewHolder holder, int position) {
        Item item = mData.get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_NEWS:
                holder.binding.setVariable(BR.item, item);
            case VIEW_TYPE_COMMENT:
                holder.binding.setVariable(BR.item, item);
            default:
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mData.get(position).getType()) {
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

    public List<Item> getData() {
        return mData;
    }

    public void swapData(List<Item> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(List<Item> data) {
        mData.addAll(data);
        notifyItemRangeInserted(mData.size() - data.size(), data.size());
    }

    class SubmissionViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        final T binding;

        public SubmissionViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class CommentViewHolder extends SubmissionViewHolder<SubmissionCommentBinding> implements View.OnClickListener {

        public CommentViewHolder(SubmissionCommentBinding binding) {
            super(binding);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Item data = mData.get(getAdapterPosition());
            ItemActivity.startActivity(mActivity, data);
        }
    }

    class ItemViewHolder extends SubmissionViewHolder<SubmissionNewsBinding> implements View.OnClickListener {

        public ItemViewHolder(SubmissionNewsBinding binding) {
            super(binding);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Item data = mData.get(getAdapterPosition());
            ItemActivity.startActivity(mActivity, data);
        }
    }
}