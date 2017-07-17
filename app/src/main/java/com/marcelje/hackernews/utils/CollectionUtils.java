package com.marcelje.hackernews.utils;

import java.util.Collections;
import java.util.List;

public class CollectionUtils {

    public static List<Long> subList(List<Long> lists, int firstIndex, int lastIndex) {
        if (lists == null || lists.size() == 0) return Collections.emptyList();
        if (firstIndex > lists.size() || lastIndex < 0) return Collections.emptyList();
        if (firstIndex > lastIndex) return Collections.emptyList();

        if (firstIndex < 0) {
            firstIndex = 0;
        }

        if (lastIndex > lists.size()) {
            lastIndex = lists.size();
        }

        return lists.subList(firstIndex, lastIndex);
    }

}
