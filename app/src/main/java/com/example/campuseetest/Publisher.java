package com.example.campuseetest;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Publisher {
    public String name = "";
    public String email = "";
    List<Event> pubEvents;
//    ArrayList<Event> myEvents = new ArrayList<Event>();
//    int followers = 0;
    public Publisher(String n, String e) {
        this.name = n;
        this.email = e;
        pubEvents=new ArrayList<>();
    }

    public List<Event> getEvents(){
        return pubEvents;
    }

    public void setEvents( Event e)
    {

        pubEvents.add(e);
    }

    public void setName(String name)
    {
        this.name=name;
    }
}
