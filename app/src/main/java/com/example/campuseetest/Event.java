package com.example.campuseetest;

import java.util.Date;

public class Event {
    private String name = "";
    private String description="";
    private int attendees=0;
    private String dateTime;
    private String location="";


    public Event(){
        dateTime=null;
    }

    public Event(String name, String description, int attendees, String dateTime, String location)
    {
        this.name=name;
        this.description=description;
        this.attendees=attendees;
        this.location=location;
        this.dateTime=dateTime;
    }

    public void setLocation(String l)
    {
        location=l;
    }

    public String getEventName()
    {

        return name;
    }

    public String getDateTime()
    {

        return dateTime;
    }

    public String getDescription()
    {
        return description;
    }

    public int getAttendees()
    {
        return attendees;
    }

    public String getLocation()
    {
        return location;
    }
}
