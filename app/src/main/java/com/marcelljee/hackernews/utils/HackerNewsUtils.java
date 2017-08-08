package com.marcelljee.hackernews.utils;

import java.util.Locale;

public class HackerNewsUtils {

    private static final String URL_HACKER_NEWS_ITEM = "https://news.ycombinator.com/item?id=%d";

    private HackerNewsUtils() {}

    public static String geItemUrl(long itemId) {
        return String.format(Locale.getDefault(), URL_HACKER_NEWS_ITEM, itemId);
    }

}
