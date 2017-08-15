package com.marcelljee.hackernews.viewmodel;

import android.support.customtabs.CustomTabsSession;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.database.DatabaseDao;
import com.marcelljee.hackernews.event.ItemBookmarkEvent;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.screen.news.item.ItemActivity;
import com.marcelljee.hackernews.screen.user.UserActivity;
import com.marcelljee.hackernews.utils.ItemUtils;
import com.marcelljee.hackernews.utils.SettingsUtils;

import org.greenrobot.eventbus.EventBus;

public class ItemNewsViewModel extends ItemViewModel {

    private final CustomTabsSession mCustomTabsSession;

    private final boolean mReadIndicatorEnabled;

    public ItemNewsViewModel(ToolbarActivity activity, boolean readIndicator, CustomTabsSession customTabsSession) {
        super(activity);
        mCustomTabsSession = customTabsSession;
        mReadIndicatorEnabled = readIndicator;
    }

    public boolean getReadIndicatorEnabled() {
        return mReadIndicatorEnabled;
    }

    public int isCommentViewVisible(Item item) {
        return item.getDescendants() > 0 ? View.VISIBLE : View.GONE;
    }

    public SpannableStringBuilder getTitle(Item item) {
        return ItemUtils.getTitle(getActivity(), item);
    }

    public void textClick(Item item) {
        if (TextUtils.isEmpty(item.getUrl())) {
            ItemActivity.startActivity(getActivity(), item);
        } else {
            CustomTabsBrowser.openTab(getActivity(), mCustomTabsSession, item.getUrl());
        }

        DatabaseDao.insertHistoryItem(getActivity(), item);
        DatabaseDao.insertReadIndicatorItem(getActivity(), item.getId());

        if (SettingsUtils.readIndicatorEnabled(getActivity())) {
            item.setRead(true);
        }
    }

    public void bookmarkClick(Item item) {
        if (DatabaseDao.isItemBookmarked(getActivity(), item.getId())) {
            DatabaseDao.deleteBookmarkedItem(getActivity(), item.getId());
            item.setBookmarked(false);
        } else {
            DatabaseDao.insertBookmarkedItem(getActivity(), item);
            item.setBookmarked(true);
        }

        if (ItemActivity.StoryActivity.class.getName().equals(getActivity().getClass().getName())) {
            EventBus.getDefault().post(new ItemBookmarkEvent.StoryActivityEvent());
        } else if (UserActivity.class.getName().equals(getActivity().getClass().getName())) {
            EventBus.getDefault().post(new ItemBookmarkEvent.UserActivityEvent());
        }
    }
}
