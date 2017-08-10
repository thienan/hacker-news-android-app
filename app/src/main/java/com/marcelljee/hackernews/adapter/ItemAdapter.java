package com.marcelljee.hackernews.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.BR;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.databinding.ItemCommentBinding;
import com.marcelljee.hackernews.databinding.ItemNewsBinding;
import com.marcelljee.hackernews.databinding.ItemPollOptionBinding;
import com.marcelljee.hackernews.handlers.ItemBookmarkClickHandlers;
import com.marcelljee.hackernews.handlers.ItemTextClickHandlers;
import com.marcelljee.hackernews.handlers.ItemUserClickHandlers;
import com.marcelljee.hackernews.menu.ActionModeMenu;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.BaseItemActivity;

import java.util.ArrayList;
import java.util.List;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;
import com.marcelljee.hackernews.utils.SettingsUtils;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public static final int VIEW_TYPE_NEWS = 1;
    public static final int VIEW_TYPE_COMMENT = 2;
    public static final int VIEW_TYPE_POLL_OPTION = 3;

    private static final String ITEM_TYPE_COMMENT = "comment";
    private static final String ITEM_TYPE_POLL_OPTION = "pollopt";
    private static final String ITEM_TYPE_STORY = "story";
    private static final String ITEM_TYPE_POLL = "poll";
    private static final String ITEM_TYPE_JOB = "job";

    private final ToolbarActivity mActivity;
    private final List<Item> mItems;
    private final String mItemParentName;
    private final String mItemPosterName;

    private final ActionModeMenu actionModeMenu;

    public ItemAdapter(ToolbarActivity activity) {
        this(activity, null, null);
    }

    public ItemAdapter(ToolbarActivity activity, String itemParentName, String itemPosterName) {
        mActivity = activity;
        mItems = new ArrayList<>();
        mItemParentName = itemParentName;
        mItemPosterName = itemPosterName;

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

                if (!UserActivity.class.getName().equals(mActivity.getClass().getName())) {
                    newsBinding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));
                }

                newsBinding.setItemTextClickHandlers(new ItemTextClickHandlers(getActivity()));
                newsBinding.setItemBookmarkClickHandlers(new ItemBookmarkClickHandlers(getActivity()));
                return new ItemViewHolder(newsBinding, true);
            case VIEW_TYPE_COMMENT:
                ItemCommentBinding commentBinding = ItemCommentBinding.inflate(inflater, parent, false);
                commentBinding.setActivity(getActivity());

                if (!UserActivity.class.getName().equals(mActivity.getClass().getName())) {
                    commentBinding.setItemUserClickHandlers(new ItemUserClickHandlers(getActivity()));
                }

                commentBinding.tvText.setMovementMethod(LinkMovementMethod.getInstance());
                return new ItemViewHolder(commentBinding, true);
            case VIEW_TYPE_POLL_OPTION:
                ItemPollOptionBinding binding = ItemPollOptionBinding.inflate(inflater, parent, false);
                return new ItemViewHolder(binding, false);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = getItem(position);
        holder.binding.setVariable(BR.item, item);
        holder.itemView.setOnLongClickListener((v) -> actionModeMenu.start(this, holder, item));
    }

    @Override
    public int getItemViewType(int position) {
        switch (getItem(position).getType()) {
            case ITEM_TYPE_COMMENT:
                return VIEW_TYPE_COMMENT;
            case ITEM_TYPE_POLL_OPTION:
                return VIEW_TYPE_POLL_OPTION;
            case ITEM_TYPE_STORY:
            case ITEM_TYPE_JOB:
            case ITEM_TYPE_POLL:
            default:
                return VIEW_TYPE_NEWS;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @SuppressWarnings("WeakerAccess")
    public Item getItem(int position) {
        return mItems.get(position);
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void swapItems(List<Item> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<Item> items) {
        mItems.addAll(items);
        notifyItemRangeInserted(mItems.size() - items.size(), items.size());
    }

    public ToolbarActivity getActivity() {
        return mActivity;
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
            itemView.setSelected(false);
            Item data = mItems.get(getAdapterPosition());
            BaseItemActivity.startActivity(mActivity, data, mItemParentName, mItemPosterName);

            switch (getItemViewType()) {
                case VIEW_TYPE_NEWS:
                    DatabaseDao.insertHistoryItem(mActivity, data);
                    DatabaseDao.insertReadIndicatorItem(mActivity, data.getId());

                    if (SettingsUtils.readIndicatorEnabled(mActivity)) {
                        ItemNewsBinding itemNewsBinding = (ItemNewsBinding) binding;
                        itemNewsBinding.svScore.setRead(true);
                    }

                    break;
                default:
                    //do nothing
            }
        }
    }
}