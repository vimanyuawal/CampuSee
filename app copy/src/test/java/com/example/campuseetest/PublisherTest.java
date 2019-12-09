package com.example.campuseetest;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class PublisherTest {
    private String name, email;
    private List<Event> pubEvents;
    @Before
    public void setUp() throws Exception {
        name="Hriday";
        email="hriday@gmail.com";
        pubEvents=new ArrayList<>();
    }

    @Test
    public void publisherTest()
    {
        Publisher p=new Publisher(name, email);
        assertEquals("Hriday", p.name);
        assertEquals("hriday@gmail.com", p.email);
        assertEquals(pubEvents, p.pubEvents);

    }

    @Test
    public void getEventsTest()
    {
        Publisher p=new Publisher(name, email);
        Event first = new Event("Expert Speaker", "Boeing Speaker", 0, "12/29/2019", "JFF");
        Event second  = new Event("Expert Speaker", "Microsoft Speaker", 0, "12/29/2019", "SAL");
        List<Event> allEvents= new ArrayList<>();
        allEvents.add(first);
        allEvents.add(second);

//        List<Event> other= new ArrayList<>();
//        Event o = new Event("Expert Speaker", "Boeing Speaker", 0, "12/29/2019", "Fertita");
//           other.add(o);

        p.setEvents(first);
        p.setEvents(second);

        assertEquals(allEvents, p.getEvents());


    }

    @Test
    public void setName()
    {
        Publisher p= new Publisher(name, email);
        p.setName("Hridee");

        assertEquals("Hridee", p.name );
    }

}