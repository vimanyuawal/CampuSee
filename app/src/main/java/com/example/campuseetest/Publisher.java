package com.example.campuseetest;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Publisher {
    public String name = "";
    public String email = "";
//    ArrayList<Event> myEvents = new ArrayList<Event>();
//    int followers = 0;
    public Publisher(String n, String e) {
        this.name = n;
        this.email = e;
    }
}
