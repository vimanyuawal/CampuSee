package com.example.campuseetest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private String name, email;

    @Before
    public void setUp() throws Exception
    {
        name="Patrick";
        email="patrickkong123@gmail.com";
    }

    @Test
    public void userTest()
    {
        User u = new User(name,email);
        assertEquals("Patrick", u.name);
        assertEquals("patrickkong123@gmail.com", u.email);
    }

    @Test
    public void setNameTest()
    {
        User u=new User(name,email);

        u.setName("Vimanyu");

        assertEquals("Vimanyu", u.name);
    }


}