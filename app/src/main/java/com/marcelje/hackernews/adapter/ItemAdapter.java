package com.marcelje.hackernews.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marcelje.hackernews.BR;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.BaseItemActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final ToolbarActivity mActivity;
    private final List<Item> mData;
    private final String mParent;
    private final String mPoster;

    public ItemAdapter(ToolbarActivity activity, String parent, String poster) {
        mActivity = activity;
        mData = new ArrayList<>();
        mParent = parent;
        mPoster = poster;
    }

    public ItemAdapter(ToolbarActivity activity) {
        mActivity = activity;
        mData = new ArrayList<>();
        mParent = null;
        mPoster = null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = getData().get(position);
        holder.binding.setVariable(BR.item, item);
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

    public class ItemViewHolder<T extends ViewDataBinding>
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final T binding;
        private final boolean mClickable;

        public ItemViewHolder(T binding, boolean clickable) {
            super(binding.getRoot());
            this.binding = binding;
            mClickable = clickable;

            if (mClickable) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            Item data = mData.get(getAdapterPosition());
            BaseItemActivity.startActivity(mActivity, data, mParent, mPoster);
        }
    }
}
