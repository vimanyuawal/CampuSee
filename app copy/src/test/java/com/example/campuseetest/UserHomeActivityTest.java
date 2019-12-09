package com.example.campuseetest;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserHomeActivityTest {

    @Test
    public void checkAdd() {
        UserHomeActivity u= new UserHomeActivity();
        assertEquals(true, u.checkAdd() );
    }
}