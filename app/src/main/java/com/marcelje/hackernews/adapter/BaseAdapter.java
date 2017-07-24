package com.marcelje.hackernews.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.model.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

    private final ToolbarActivity mActivity;
    private final List<Item> mData;

    public BaseAdapter(ToolbarActivity mActivity) {
        this.mActivity = mActivity;
        mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected ToolbarActivity getActivity() {
        return mActivity;
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

    public static class BaseViewHolder<T extends ViewDataBinding>
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final T binding;
        private final OnClickListener mListener;

        public BaseViewHolder(T binding, OnClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            mListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onClick(getAdapterPosition());
            }
        }
    }

    public interface OnClickListener {
        void onClick(int pos);
    }

}
