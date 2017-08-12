package com.marcelljee.hackernews.menu;

import android.support.v7.app.AppCompatActivity;
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
import com.marcelljee.hackernews.utils.SettingsUtils;

public class ActionModeMenu {

    private ActionMode mActionMode;
    private long itemId;

    public boolean start(AppCompatActivity activity, ItemAdapter.ItemViewHolder holder, Item item) {
        finish();

        if (itemId == item.getId()) {
            itemId = Item.NO_ID;
            return false;
        }

        switch (holder.getItemViewType()) {
            case ItemAdapter.VIEW_TYPE_NEWS:
                ItemNewsBinding binding = (ItemNewsBinding) holder.binding;
                binding.ivSelected.setVisibility(View.VISIBLE);

                itemId = item.getId();
                mActionMode = activity.startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.menu_context_story, menu);

                        if (!SettingsUtils.readIndicatorEnabled(activity)) {
                            menu.findItem(R.id.action_mark_read).setVisible(false);
                            menu.findItem(R.id.action_mark_unread).setVisible(false);
                        }

                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        if (DatabaseDao.isItemRead(activity, item.getId())) {
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
                                MenuUtils.openShareHackerNewsLinkChooser(activity, item);
                                return true;
                            case R.id.action_mark_read:
                                DatabaseDao.insertReadIndicatorItem(activity, item.getId());
                                binding.svScore.setRead(true);
                                binding.ivSelected.setRead(true);
                                mode.finish();
                                return true;
                            case R.id.action_mark_unread:
                                DatabaseDao.deleteReadIndicatorItem(activity, item.getId());
                                binding.svScore.setRead(false);
                                binding.ivSelected.setRead(false);
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

    public void finish() {
        if (isStarted()) {
            mActionMode.finish();
        }
    }

    private boolean isStarted() {
        return mActionMode != null;
    }
}
