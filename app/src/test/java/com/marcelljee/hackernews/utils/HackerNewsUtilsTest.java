package com.marcelljee.hackernews.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class HackerNewsUtilsTest {

    @Test
    public void testGetItemUrl_itemIdEqualsMin100() {
        String url = "https://news.ycombinator.com/item?id=-100";
        String result = HackerNewsUtils.getItemUrl(-100);
        assertEquals(result, url);
    }

    @Test
    public void testGetItemUrl_itemIdEquals1() {
        String url = "https://news.ycombinator.com/item?id=1";
        String result = HackerNewsUtils.getItemUrl(1);
        assertEquals(result, url);
    }

    @Test
    public void testGetItemUrl_itemIdEquals100() {
        String url = "https://news.ycombinator.com/item?id=100";
        String result = HackerNewsUtils.getItemUrl(100);
        assertEquals(result, url);
    }
}