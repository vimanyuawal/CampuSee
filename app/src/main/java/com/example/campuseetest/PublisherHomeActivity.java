package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



import android.view.ViewGroup;
import android.widget.TableRow.LayoutParams;
import android.widget.TableRow;
import android.view.ViewGroup.*;




import org.w3c.dom.Text;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublisherHomeActivity extends AppCompatActivity {

    String identifierVal;

    String impKey;

    String finalVal="Does it work: ";

    GoogleSignInClient mGoogleSignInClient;

    String x="test";
    String y="test";

//    final List<Event> curEvents=new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_home);

        identifierVal = getIntent().getStringExtra("identifier");

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(PublisherHomeActivity.this);

        identifierVal=acct.getDisplayName();


        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mPublisherRef = mRootRef.child("Publisher");

        mPublisherRef.orderByChild("name").equalTo(identifierVal).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas: dataSnapshot.getChildren()) {

                    DatabaseReference mEventsRef=mRootRef.child("Publisher").child(datas.getKey()).child("Event").getRef();

                    mEventsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String xyz="";

                            List<Event> curEvents=new ArrayList<Event>();
                            for (DataSnapshot datas2: snapshot.getChildren()) {
                                    String x=datas2.child("eventName").getValue(String.class);
                                    xyz+=x+" ";

                                    Event current=new Event(datas2.child("eventName").getValue(String.class),datas2.child("description").getValue(String.class),datas2.child("attendees").getValue(int.class),null,datas2.child("location").getValue(String.class));

                                    curEvents.add(current);
                            }

                            init(curEvents);

//                            TextView showName=(TextView)findViewById(R.id.nameShowing);
//                            showName.setText("Name: "+xyz);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        //Events ArrayList is finished (curEvents)
//        init();


    }

//    public void sendMessage(View view) {
//        // Do something in response to button click
//        Intent intent = new Intent(PublisherHomeActivity.this, AddEventActivity.class);
//        intent.putExtra("identifier",identifierVal);
//        startActivity(intent);
//    }


    public void init(List<Event> curEvents)
    {
        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout1);

        TableRow row= new TableRow(this);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 60);

        TextView eventTitle= new TextView(this);
        eventTitle.setText("Event");
        eventTitle.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        eventTitle.setWidth(50);
        TextView timeTitle= new TextView(this);
        timeTitle.setText("Location");
        timeTitle.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        timeTitle.setWidth(50);
        TextView locationTitle= new TextView(this);
        locationTitle.setText("Time");
        locationTitle.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        locationTitle.setWidth(50);

//        TextView attendees= new TextView(this);
//        attendees.setText("Attendees");
//        attendees.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
//        attendees.setWidth(20);

        row.addView(eventTitle);
        row.addView(timeTitle);
        row.addView(locationTitle);
//        row.addView(attendees);


        ll.addView(row);




        for(int i=0; i < curEvents.size(); i++)
        {

            row= new TableRow(this);
            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 60);
            row.setLayoutParams(lp);


            TextView event= new TextView(this);
            event.setText(curEvents.get(i).getEventName());
            event.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));

            event.setWidth(50);
            event.setMaxLines(1);
            event.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                event.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }
            event.setClickable(true);
            event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(PublisherHomeActivity.this, AddEventActivity.class);
                    intent.putExtra("identifier",identifierVal);
                    startActivity(intent);

                }
            });
            TextView location= new TextView(this);
            location.setText(curEvents.get(i).getLocation());
            location.setWidth(50);
            location.setMaxLines(1);
            location.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
            location.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                location.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }


            TextView date= new TextView(this);
            date.setText("11/2/2019 7:00 PM");

            date.setWidth(50);
            date.setMaxLines(1);
            date.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
            date.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }

//            TextView numAttend= new TextView(this);
//            numAttend.setText(curEvents.get(i).getAttendees());
//
//
//
//            numAttend.setWidth(50);
//            numAttend.setMaxLines(1);
//            numAttend.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
//            numAttend.setPadding(12,12,12,12);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                numAttend.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
//            }

            row.addView(event);
            row.addView(location);
            row.addView(date);
//            row.addView(numAttend);

            ll.addView(row);
        }
    }
}
