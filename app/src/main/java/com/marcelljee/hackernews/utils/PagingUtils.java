package com.marcelljee.hackernews.utils;

import java.util.List;

public final class PagingUtils {

    private static final int ITEM_COUNT = 10;

    private PagingUtils() {}

    public static List<Long> getItems(List<Long> items, int currentPage) {
        if (currentPage < 1) currentPage = 1;

        return CollectionUtils.subList(items,
                (currentPage - 1) * ITEM_COUNT,
                currentPage * ITEM_COUNT);
    }

}
