package com.marcelljee.hackernews.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionUtilsTest {

    private List<Long> items = new ArrayList<>();
    private List<Long> firstPages = new ArrayList<>();
    private List<Long> fifthPages = new ArrayList<>();
    private List<Long> tenthPages = new ArrayList<>();

    @Before
    public void setUp() {
        for (long i = 1; i < 95; i++) {
            items.add(i);
        }

        for (long i = 1; i <= 10; i++) {
            firstPages.add(i);
        }

        for (long i = 51; i <= 55; i++) {
            fifthPages.add(i);
        }

        for (long i = 91; i < 95; i++) {
            tenthPages.add(i);
        }
    }

    @Test
    public void testSubList_min100ToMin10() {
        List<Long> results = CollectionUtils.subList(items, -100, -10);
        assertEquals(0, results.size());
    }

    @Test
    public void testSubList_30To20() {
        List<Long> results = CollectionUtils.subList(items, 30, 20);
        assertEquals(0, results.size());
    }

    @Test
    public void testSubList_min5To10() {
        List<Long> results = CollectionUtils.subList(items, -5, 10);
        assertEquals(10, results.size());
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testSubList_0To10() {
        List<Long> results = CollectionUtils.subList(items, 0, 10);
        assertEquals(10, results.size());
        assertTrue(results.equals(firstPages));
    }

    @Test
    public void testSubList_50To55() {
        List<Long> results = CollectionUtils.subList(items, 50, 55);
        assertEquals(5, results.size());
        assertTrue(results.equals(fifthPages));
    }

    @Test
    public void testSubList_91To100() {
        List<Long> results = CollectionUtils.subList(items, 90, 100);
        assertEquals(4, results.size());
        assertTrue(results.equals(tenthPages));
    }

    @Test
    public void testSubList_min10To1000() {
        List<Long> results = CollectionUtils.subList(items, -10, 1000);
        assertEquals(94, results.size());
        assertTrue(results.equals(items));
    }

    @Test
    public void testSubList_200To300() {
        List<Long> results = CollectionUtils.subList(items, 200, 300);
        assertEquals(0, results.size());
    }

}