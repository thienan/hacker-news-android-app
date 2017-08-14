package com.marcelljee.hackernews.event;

import com.marcelljee.hackernews.model.Item;

public final class ItemRefreshEvent {

    private final Item item;

    public ItemRefreshEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
