package com.example.campuseetest;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

//Client ID: 344394877418-f59t5a8u1mnm6cek0vvuhlr0ehf449p9.apps.googleusercontent.com
//Client Secret: I3i6MNßmBj_BNkrHvuSgNVaJS
//For google sign in
@IgnoreExtraProperties
public class User {

    public String name = "";
    public String email = "";
//    public ArrayList<Publisher> following = new ArrayList<Publisher>();
//    public ArrayList<Event> myEvents = new ArrayList<Event>();

    public User(String n, String e) {
        this.name = n;
        this.email = e;
    }

    public void setName(String name)
    {
        this.name=name;
    }


}
