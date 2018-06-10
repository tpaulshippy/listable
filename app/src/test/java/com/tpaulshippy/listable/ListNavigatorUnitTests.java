package com.tpaulshippy.listable;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ListNavigatorUnitTests {
    @Test
    public void GetItems_ReturnsSingle() {
        String input = "Test 1";
        ArrayList list = ListNavigatorService.GetItems(input);
        assertEquals(1, list.size());
        assertEquals("Test 1", list.get(0));
    }
    @Test
    public void GetItems_ParsesLines() {
        String input = "Test 1" + System.lineSeparator() +
                "Test 2" + System.lineSeparator() +
                "Test 3";
        ArrayList list = ListNavigatorService.GetItems(input);
        assertEquals(3, list.size());
        assertEquals("Test 1", list.get(0));
        assertEquals("Test 2", list.get(1));
        assertEquals("Test 3", list.get(2));
    }
    @Test
    public void GetItems_RemovesEmpty() {
        String input = "Test 1" + System.lineSeparator() +
                "   " + System.lineSeparator() +
                "Test 2" + System.lineSeparator() +
                System.lineSeparator() +
                "Test 3";
        ArrayList list = ListNavigatorService.GetItems(input);
        assertEquals(3, list.size());
        assertEquals("Test 1", list.get(0));
        assertEquals("Test 2", list.get(1));
        assertEquals("Test 3", list.get(2));
    }
    @Test
    public void GetItems_RemovesSpecial() {
        String input = "- Test 1" + System.lineSeparator() +
                "- Test 2" + System.lineSeparator() +
                "- Test 3";
        ArrayList list = ListNavigatorService.GetItems(input);
        assertEquals(3, list.size());
        assertEquals("Test 1", list.get(0));
        assertEquals("Test 2", list.get(1));
        assertEquals("Test 3", list.get(2));
    }
}