package com.marcelje.hackernews.screen.news.comment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.databinding.ItemCommentBinding;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final ToolbarActivity mActivity;
    private final String mParent;
    private final String mPoster;
    private final List<Item> mData;

    public CommentAdapter(ToolbarActivity activity, String parent, String poster) {
        mActivity = activity;
        mParent = parent;
        mPoster = poster;

        mData = new ArrayList<>();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding binding = ItemCommentBinding.inflate(inflater, parent, false);
        binding.setItemUserClickHandlers(new ItemUserClickHandlers(mActivity));

        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.binding.setActivity(mActivity);
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(List<Item> data) {
        mData.addAll(data);
        notifyItemRangeInserted(mData.size() - data.size(), data.size());
    }

    class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ItemCommentBinding binding;

        public CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Item data = mData.get(getAdapterPosition());
            CommentActivity.startActivity(mActivity, data, mParent, mPoster);
        }
    }
}