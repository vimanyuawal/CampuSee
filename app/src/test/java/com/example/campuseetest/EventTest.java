package com.example.campuseetest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {
    private String name, description, time, location;
    private Integer attendees;

    @Before
    public void setUp() throws Exception {

        name = "Expert Talk";
        description = "Executive for Boeing";
        time = "12/23/2019";
        attendees = 0;
        location = "JFF";

    }

    @Test
    public void eventTest() {
        Event e = new Event(name, description, attendees, time, location);

        assertEquals("Expert Talk", e.getEventName());
        assertEquals("Executive for Boeing", e.getDescription());
        assertEquals("12/23/2019", e.getDateTime());
        assertEquals(0, e.getAttendees());
        assertEquals("JFF", e.getLocation());

    }

    @Test
    public void setLocationTest() {
        String set = "SAL";
        Event e = new Event(name, description, attendees, time, location);
        e.setLocation(set);
        assertEquals(set, e.getLocation());
    }

    @Test
    public void getEventNameTest()
    {
        Event e = new Event(name, description, attendees, time, location);
        assertEquals(name, e.getEventName());
    }

    @Test
    public void getDescriptionTest()
    {
        Event e = new Event(name, description, attendees, time, location);
        assertEquals(description, e.getDescription());
    }

    @Test
    public void getAttendeesTest()
    {
        Event e = new Event(name, description, attendees, time, location);
        assertEquals( attendees, (Integer) e.getAttendees());
    }


    @Test
    public void getDateTimeTest()
    {
        Event e = new Event(name, description, attendees, time, location);
        assertEquals( time,  e.getDateTime());
    }

    @Test
    public void getLocationTest()
    {
        Event e = new Event(name, description, attendees, time, location);
        assertEquals(location, e.getLocation());
    }


}