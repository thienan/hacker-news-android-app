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
import com.marcelljee.hackernews.menu.ActionModeMenu;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.utils.SettingsUtils;
import com.marcelljee.hackernews.viewmodel.ItemCommentViewModel;
import com.marcelljee.hackernews.viewmodel.ItemNewsViewModel;

import io.reactivex.Observable;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private static final int VIEW_TYPE_NEWS = 1;
    private static final int VIEW_TYPE_COMMENT = 2;
    private static final int VIEW_TYPE_POLL_OPTION = 3;

    private static final String ITEM_TYPE_COMMENT = "comment";
    private static final String ITEM_TYPE_POLL_OPTION = "pollopt";
    private static final String ITEM_TYPE_STORY = "story";
    private static final String ITEM_TYPE_POLL = "poll";
    private static final String ITEM_TYPE_JOB = "job";

    private final ToolbarActivity mActivity;
    private final List<Item> mItems;
    private final Map<Integer, Item> mReadItems;
    private final String mItemParentName;
    private final String mItemPosterName;

    private final ActionModeMenu mActionModeMenu;

    private boolean isShowAll = true;

    public ItemAdapter(ToolbarActivity activity) {
        this(activity, null, null);
    }

    public ItemAdapter(ToolbarActivity activity, String itemParentName, String itemPosterName) {
        mActivity = activity;
        mReadItems = new LinkedHashMap<>();
        mItems = new ArrayList<>();
        mItemParentName = itemParentName;
        mItemPosterName = itemPosterName;

        mActionModeMenu = new ActionModeMenu(getActivity());
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_NEWS:
                ItemNewsBinding newsBinding = ItemNewsBinding.inflate(inflater, parent, false);
                newsBinding.setViewModel(new ItemNewsViewModel(getActivity(), true, null));
                return new ItemViewHolder(newsBinding, true);
            case VIEW_TYPE_COMMENT:
                ItemCommentBinding commentBinding = ItemCommentBinding.inflate(inflater, parent, false);
                commentBinding.setViewModel(new ItemCommentViewModel(getActivity()));
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
        item.setBookmarked(DatabaseDao.isItemBookmarked(mActivity, item.getId()));
        holder.binding.setVariable(BR.item, item);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_NEWS:
                ItemNewsBinding newsBinding = (ItemNewsBinding) holder.binding;
                newsBinding.svScore.setOnClickListener((v) -> mActionModeMenu.start(newsBinding));
                newsBinding.getRoot().setOnLongClickListener((v) -> mActionModeMenu.start(newsBinding));
                break;
            default:
                //do nothing
        }
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
        clearItems();
        addItems(items);
    }

    public void clearItems() {
        int num = mItems.size();
        mItems.clear();
        mReadItems.clear();
        notifyItemRangeRemoved(0, num);
    }

    public void addItems(List<Item> items) {
        Observable.fromIterable(items)
                .map(item -> {
                    item.setBookmarked(DatabaseDao.isItemBookmarked(mActivity, item.getId()));
                    item.setRead(DatabaseDao.isItemRead(mActivity, item.getId()));
                    return item;
                }).toList()
                .subscribe(itemList -> {
                    if (isShowAll) {
                        insertAll(itemList);
                    } else {
                        insertUnread(itemList);
                    }
                });
    }

    private void insertAll(List<Item> items) {
        mItems.addAll(items);
        notifyItemRangeInserted(mItems.size() - items.size(), items.size());
    }

    private void insertUnread(List<Item> items) {
        for (int pos = 0; pos < items.size(); pos++) {
            Item item = items.get(pos);

            if (item.isRead()) {
                mReadItems.put(mItems.size() + mReadItems.size(), item);
            } else {
                mItems.add(item);
                notifyItemInserted(mItems.size());
            }
        }
    }

    public void showAll() {
        isShowAll = true;

        Map<Integer, Item> readItems = new LinkedHashMap<>(mReadItems);

        for (Map.Entry<Integer, Item> readItem : readItems.entrySet()) {
            mReadItems.remove(readItem.getKey());
            mItems.add(readItem.getKey(), readItem.getValue());
            notifyItemInserted(readItem.getKey());
        }
    }

    public void showUnread() {
        isShowAll = false;

        List<Item> allItems = new ArrayList<>(mItems);

        for (int pos = 0; pos < allItems.size(); pos++) {
            Item item = allItems.get(pos);

            if (item.isRead()) {
                mReadItems.put(pos, item);
                notifyItemRemoved(mItems.indexOf(item));
                mItems.remove(item);
            }
        }
    }

    public void clearReadIndicator() {
        for (Item item : mItems) {
            item.setRead(false);
        }
    }

    public ToolbarActivity getActivity() {
        return mActivity;
    }

    public void closeActionModeMenu() {
        mActionModeMenu.finish();
    }

    class ItemViewHolder<T extends ViewDataBinding>
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final T binding;
        private final boolean mClickable;

        ItemViewHolder(T binding, boolean clickable) {
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
            Item item = mItems.get(getAdapterPosition());
            ItemActivity.startActivity(mActivity, item, mItemParentName, mItemPosterName);

            switch (getItemViewType()) {
                case VIEW_TYPE_NEWS:
                    DatabaseDao.insertHistoryItem(mActivity, item);
                    DatabaseDao.insertReadIndicatorItem(mActivity, item.getId());

                    if (SettingsUtils.readIndicatorEnabled(mActivity)) {
                        item.setRead(true);
                    }

                    break;
                default:
                    //do nothing
            }
        }
    }
}
