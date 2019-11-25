package com.example.campuseetest;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        float zoomLevel = (float) 15.0;
        LatLng center = new LatLng(34.021196, -118.285422);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference publisher = root.child("Publisher");

        publisher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datas: snapshot.getChildren()) {
                    DatabaseReference mEventsRef=publisher.child(datas.getKey()).child("Event").getRef();


                    mEventsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String tester="";
                            for(DataSnapshot datas2: dataSnapshot.getChildren()){
                                String location=datas2.child("location").getValue(String.class);
                                String description = datas2.child("eventName").getValue(String.class);

                                if(location.equals("RTH")){

                                    LatLng latlng = new LatLng(34.0201,
                                            -118.2899);
                                    googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(description));

                                }

                                if(location.equals("JFF")){
                                    LatLng latlng = new LatLng(34.0187,
                                            -118.2824);
                                    googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(description));
                                }

                                if(location.equals("THH")){

                                    LatLng latlng = new LatLng(34.0222,
                                            -118.2846);

                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                    googleMap.addMarker(new MarkerOptions().position(latlng)
                                            .title(description));

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    String l = "";

    public void setLocation(String loc){
        l = loc;
    }

    public String getLocation(){
        return l;
    }

}

