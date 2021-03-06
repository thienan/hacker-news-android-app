package com.marcelljee.hackernews.event;

import com.marcelljee.hackernews.model.Item;

public final class ItemRefreshEvent {

    private final Item mItem;

    public ItemRefreshEvent(Item item) {
        mItem = item;
    }

    public Item getItem() {
        return mItem;
    }
}
