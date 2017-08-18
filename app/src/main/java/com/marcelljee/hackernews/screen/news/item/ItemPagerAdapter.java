package com.marcelljee.hackernews.screen.news.item;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.view.ScrollableViewPager;

import java.util.List;

class ItemPagerAdapter extends FragmentStatePagerAdapter {

    private final ViewPager mItemPager;
    private final List<Item> mItems;
    private final Item mPosterItem;

    private int mCurrentPosition = -1;

    public ItemPagerAdapter(ViewPager itemPager, FragmentManager fm, List<Item> items, Item posterItem) {
        super(fm);
        mItemPager = itemPager;
        mItems = items;
        mPosterItem = posterItem;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemFragment.newInstance(mItems.get(position), mPosterItem, position);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            ScrollableViewPager pager = (ScrollableViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }

    public ItemFragment getCurrentFragment() {
        return (ItemFragment) instantiateItem(mItemPager, mItemPager.getCurrentItem());
    }
}
