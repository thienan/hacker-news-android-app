package com.marcelljee.hackernews.menu;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.databinding.ItemNewsBinding;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.HackerNewsUtils;
import com.marcelljee.hackernews.utils.MenuUtils;
import com.marcelljee.hackernews.utils.SettingsUtils;

public class ActionModeMenu {

    private long itemId;
    private ActionMode mActionMode;

    public boolean start(AppCompatActivity activity, ItemNewsBinding binding, Item item) {
        if (itemId == item.getId()) {
            finish();
            return false;
        } else {
            finish();
        }

        binding.ivSelected.setVisibility(View.VISIBLE);
        binding.getRoot().setSelected(true);

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
                if (DatabaseDao.isItemRead(activity, itemId)) {
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
                        DatabaseDao.insertReadIndicatorItem(activity, itemId);
                        binding.svScore.setRead(true);
                        binding.ivSelected.setRead(true);
                        mode.finish();
                        return true;
                    case R.id.action_mark_unread:
                        DatabaseDao.deleteReadIndicatorItem(activity, itemId);
                        binding.svScore.setRead(false);
                        binding.ivSelected.setRead(false);
                        mode.finish();
                        return true;
                    case R.id.action_open_page:
                        CustomTabsBrowser.openTab(activity,
                                HackerNewsUtils.geItemUrl(itemId));
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                binding.ivSelected.setVisibility(View.GONE);
                binding.getRoot().setSelected(false);

                itemId = Item.NO_ID;
                mActionMode = null;
            }
        });

        return true;
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
