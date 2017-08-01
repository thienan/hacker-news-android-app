package co.marcelje.hackernews.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {

    private CollectionUtils() {}

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

    public static long[] toLongArray(List<Long> list) {
        if (list == null) return new long[0];

        long[] array = new long[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static List<Long> toLongList(long[] array) {
        List<Long> list = new ArrayList<>();
        if (array == null) return list;

        for (long number : array) {
            list.add(number);
        }

        return list;
    }

}
