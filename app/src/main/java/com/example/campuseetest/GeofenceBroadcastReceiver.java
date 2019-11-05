package com.example.campuseetest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    // ...
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("d", "Did i get here?");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "whoops";
            Log.e("", errorMessage);
            return;
        }

        Log.d("d", "Did i get here?");

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        Log.d("a", "transition: " + geofenceTransition);
        Log.d("a", "transition: " + Geofence.GEOFENCE_TRANSITION_ENTER);
        Log.d("a", "transition: " + Geofence.GEOFENCE_TRANSITION_EXIT);


        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = "damn " + triggeringGeofences;


            // Send notification and log the transition details.
            Log.d("a", geofenceTransitionDetails);
            sendNotification(geofenceTransitionDetails);

        } else {
            // Log the error.
            Log.e("", "Didn't make it");
        }
    }

    private void sendNotification(String geofenceTransitionDetails) {

        Log.d("Entered", "Entered");

    }
}
