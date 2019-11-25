package com.example.campuseetest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTest {
 private String location;
    @Before
    public void setUp() throws Exception {
        location="JFF";
    }

    @Test
    public void getLocationTest() {

        Map l= new Map();
        l.setLocation(location);

        assertEquals(location, l.getLocation());
    }
}