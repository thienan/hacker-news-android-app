package com.marcelljee.hackernews.event;

import com.marcelljee.hackernews.model.Item;

public class ItemUpdateEvent {

    private Item mItem;

    public ItemUpdateEvent(Item item) {
        mItem = item;
    }

    public Item getItem() {
        return mItem;
    }
}
