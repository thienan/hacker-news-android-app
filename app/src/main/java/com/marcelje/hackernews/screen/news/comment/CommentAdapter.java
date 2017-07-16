package com.marcelje.hackernews.screen.news.comment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.ItemCommentBinding;
import com.marcelje.hackernews.handlers.ItemUserClickHandlers;
import com.marcelje.hackernews.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Activity mActivity;
    private final String mParent;
    private final String mPoster;
    private final List<Item> mData;

    public CommentAdapter(Activity activity, String parent, String poster) {
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
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(Item data) {
        mData.add(data);
        notifyItemInserted(mData.size());
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