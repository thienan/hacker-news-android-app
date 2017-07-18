package com.marcelje.hackernews.utils;

public class HackerNewsUtils {

    private static final String URL_HACKER_NEWS_ITEM = "https://news.ycombinator.com/item?id=%d";

    public static String geItemUrl(long itemId) {
        return String.format(URL_HACKER_NEWS_ITEM, itemId);
    }

}
