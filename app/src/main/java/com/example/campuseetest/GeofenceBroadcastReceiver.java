package com.example.campuseetest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    // ...
    String TAG = "BroadCast";
    private String userEmail;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);

        userEmail = acct.getEmail();
        String key = userEmail.replace('.', ',');

        Log.d(TAG, "In on receive");
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.

                List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

                Log.d(TAG, "Entire triggeringGeofences: " + triggeringGeofences.toString());

                Log.d(TAG, "Entering geofence: " + triggeringGeofences.get(0).toString());

                DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                root.child("User").child(key).child("Location").setValue(triggeringGeofences.get(0).getRequestId());

        }

        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Log.d(TAG, "Leaving geofence " + triggeringGeofences.get(0).toString());

            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            root.child("User").child(key).child("Location").setValue("outside");

        }

        else {
            // Log the error.
        }
    }

    private void sendNotification(String geofenceTransitionDetails) {

        Log.d("Entered", "Entered");

    }
}
