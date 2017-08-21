package com.marcelljee.hackernews.utils;

import android.content.Intent;

import com.marcelljee.hackernews.screen.user.UserActivity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

import static org.junit.Assert.*;

public class PagingUtilsTest {

    private List<Long> items = new ArrayList<>();
    List<Long> firstPages = new ArrayList<>();
    List<Long> fifthPages = new ArrayList<>();
    List<Long> tenthPages = new ArrayList<>();

    @Before
    public void setUp() {
        for (long i = 1; i < 95; i++) {
            items.add(i);
        }

        for (long i = 1; i <= 10; i++) {
            firstPages.add(i);
        }

        for (long i = 51; i <= 60; i++) {
            fifthPages.add(i);
        }

        for (long i = 91; i < 95; i++) {
            tenthPages.add(i);
        }
    }

    @Test
    public void testGetItems_minusPage() {
        List<Long> results = PagingUtils.getItems(items, -8);
        assertEquals(results.size(), 10);
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testGetItems_zeroPage() {
        List<Long> results = PagingUtils.getItems(items, 0);
        assertEquals(results.size(), 10);
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testGetItems_firstPage() {
        List<Long> results = PagingUtils.getItems(items, 1);
        assertEquals(results.size(), 10);
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testGetItems_tenthPage() {
        List<Long> results = PagingUtils.getItems(items, 10);
        assertEquals(results.size(), 4);
        assertTrue(results.equals(tenthPages));
    }

    @Test
    public void testGetItems_eleventhPage() {
        List<Long> results = PagingUtils.getItems(items, 11);
        assertEquals(results.size(), 0);
    }

}