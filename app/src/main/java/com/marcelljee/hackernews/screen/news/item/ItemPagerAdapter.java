package com.marcelljee.hackernews.screen.news.item;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.marcelljee.hackernews.model.Item;

import java.util.List;

class ItemPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Item> mItems;
    private final String mItemPosterName;

    public ItemPagerAdapter(FragmentManager fm, List<Item> mItems, String mItemPosterName) {
        super(fm);
        this.mItems = mItems;
        this.mItemPosterName = mItemPosterName;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemFragment.newInstance(mItems.get(position), mItemPosterName, position);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public ItemFragment getCurrentFragment(ViewPager itemPager) {
        return (ItemFragment) instantiateItem(itemPager, itemPager.getCurrentItem());
    }
}
