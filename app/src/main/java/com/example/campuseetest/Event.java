package com.example.campuseetest;

import java.util.Date;

public class Event {
    private String name = "";
    private String description="";
    private int attendees=0;
    private Date dateTime;
    private String location="";

    public Event(){
        dateTime=null;
    }

    public Event(String name, String description, int attendees, Date dateTime, String location)
    {
        this.name=name;
        this.description=description;
        this.attendees=attendees;
        this.dateTime=dateTime;
        this.location=location;

    }

    public void setLocation(String l)
    {
        location=l;
    }

    public String getEventName()
    {

        return name;
    }

    public Date getDateTime()
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
