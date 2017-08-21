package com.marcelljee.hackernews.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

public class PagingUtilsTest {

    private final List<Long> items = new ArrayList<>();
    private final List<Long> firstPages = new ArrayList<>();
    private final List<Long> tenthPages = new ArrayList<>();

    @Before
    public void setUp() {
        for (long i = 1; i < 95; i++) {
            items.add(i);
        }

        for (long i = 1; i <= 10; i++) {
            firstPages.add(i);
        }

        for (long i = 91; i < 95; i++) {
            tenthPages.add(i);
        }
    }

    @Test
    public void testGetItems_minusPage() {
        List<Long> results = PagingUtils.getItems(items, -8);
        assertEquals(10, results.size());
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testGetItems_zeroPage() {
        List<Long> results = PagingUtils.getItems(items, 0);
        assertEquals(10, results.size());
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testGetItems_firstPage() {
        List<Long> results = PagingUtils.getItems(items, 1);
        assertEquals(10, results.size());
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testGetItems_tenthPage() {
        List<Long> results = PagingUtils.getItems(items, 10);
        assertEquals(4, results.size());
        assertTrue(results.equals(tenthPages));
    }

    @Test
    public void testGetItems_eleventhPage() {
        List<Long> results = PagingUtils.getItems(items, 11);
        assertEquals(0, results.size());
    }
}