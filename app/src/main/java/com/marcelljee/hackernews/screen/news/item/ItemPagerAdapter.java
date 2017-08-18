package com.marcelljee.hackernews.screen.news.item;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.marcelljee.hackernews.model.Item;

import java.util.List;

class ItemPagerAdapter extends FragmentStatePagerAdapter {

    private final ViewPager mItemPager;
    private final List<Item> mItems;
    private final Item mPosterItem;

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

    public ItemFragment getCurrentFragment() {
        return (ItemFragment) instantiateItem(mItemPager, mItemPager.getCurrentItem());
    }
}
