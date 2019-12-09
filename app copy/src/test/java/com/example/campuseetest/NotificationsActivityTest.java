package com.example.campuseetest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationsActivityTest {
private String n;
    @Before
    public void setUp() throws Exception {
         n="Test Notification";
    }

    @Test
    public void getNotification() {

        NotificationsActivity notification= new NotificationsActivity();
        notification.setNotification(n);

        assertEquals(n, notification.getNotification());
    }
}