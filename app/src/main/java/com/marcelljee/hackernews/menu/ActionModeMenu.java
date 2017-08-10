package com.marcelljee.hackernews.menu;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.adapter.ItemAdapter;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.databinding.ItemNewsBinding;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.MenuUtils;

public class ActionModeMenu {

    private ActionMode mActionMode;

    public boolean start(ItemAdapter adapter, ItemAdapter.ItemViewHolder holder, Item item) {
        if (mActionMode != null) {
            mActionMode.finish();
        }

        switch (adapter.getItemViewType(holder.getAdapterPosition())) {
            case ItemAdapter.VIEW_TYPE_NEWS:
                ItemNewsBinding binding = (ItemNewsBinding) holder.binding;
                binding.ivSelected.setVisibility(View.VISIBLE);

                mActionMode = adapter.getActivity().startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.menu_context_story, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        if (DatabaseDao.isItemRead(adapter.getActivity(),
                                adapter.getItemId(holder.getAdapterPosition()))) {
                            menu.findItem(R.id.action_mark_read).setVisible(false);
                        } else {
                            menu.findItem(R.id.action_mark_unread).setVisible(false);
                        }

                        return true;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_share:
                                MenuUtils.openShareHackerNewsLinkChooser(adapter.getActivity(), item);
                                return true;
                            case R.id.action_mark_read:
                                DatabaseDao.insertReadIndicatorItem(adapter.getActivity(),
                                        adapter.getItemId(holder.getAdapterPosition()));
                                binding.svScore.setRead(true);
                                adapter.notifyItemChanged(holder.getAdapterPosition(), binding.ivSelected);
                                mode.finish();
                                return true;
                            case R.id.action_mark_unread:
                                DatabaseDao.deleteReadIndicatorItem(adapter.getActivity(),
                                        adapter.getItemId(holder.getAdapterPosition()));
                                binding.svScore.setRead(false);
                                adapter.notifyItemChanged(holder.getAdapterPosition(), binding.ivSelected);
                                mode.finish();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        binding.ivSelected.setVisibility(View.GONE);
                        holder.itemView.setSelected(false);
                        mActionMode = null;
                    }
                });
                holder.itemView.setSelected(true);
                return true;
            default:
                return false;
        }
    }
}
