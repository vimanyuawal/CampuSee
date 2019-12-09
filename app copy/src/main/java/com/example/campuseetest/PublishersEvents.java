package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublishersEvents extends AppCompatActivity {

    String identifierVal;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishers_events);

        identifierVal = getIntent().getStringExtra("publisherId");

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        acct = GoogleSignIn.getLastSignedInAccount(PublishersEvents.this);


        Context context = getApplicationContext();
        CharSequence text = identifierVal;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //refer to DB to show all events


        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        String key=identifierVal.replace('.', ',');

        DatabaseReference mEventsRef=mRootRef.child("Publisher").child(key).child("Event").getRef();

        mEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String xyz="";

                List<Event> curEvents=new ArrayList<Event>();
                for (DataSnapshot datas2: snapshot.getChildren()) {
                    String x=datas2.child("eventName").getValue(String.class);
                    xyz+=x+" ";

                    Event current=new Event(datas2.child("eventName").getValue(String.class),datas2.child("description").getValue(String.class),datas2.child("attendees").getValue(Integer.class),datas2.child("dateTime").getValue(String.class),datas2.child("location").getValue(String.class));

                    curEvents.add(current);
                }

                init(curEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void init(final List<Event> curEvents)
    {
        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout1);

        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);

        TextView eventTitle= new TextView(this);
        eventTitle.setText("Event");
        eventTitle.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        eventTitle.setWidth(50);
        TextView timeTitle= new TextView(this);
        timeTitle.setText("Location");
        timeTitle.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        timeTitle.setWidth(50);
        TextView locationTitle= new TextView(this);
        locationTitle.setText("Time");
        locationTitle.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        locationTitle.setWidth(50);

        TextView attendees= new TextView(this);
        attendees.setText("Attendees");
        attendees.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        attendees.setWidth(50);

        row.addView(eventTitle);
        row.addView(timeTitle);
        row.addView(locationTitle);
        row.addView(attendees);

        ll.addView(row);

        for(int i=0; i < curEvents.size(); i++)
        {

            row= new TableRow(this);
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);
            row.setLayoutParams(lp);


            TextView event= new TextView(this);

            String setterText=curEvents.get(i).getEventName();
            SpannableString content=new SpannableString(setterText);
            content.setSpan(new UnderlineSpan(),0,setterText.length(),0);

            event.setText(content);
            event.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));

            event.setWidth(50);
            event.setMaxLines(1);
            event.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                event.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }
            event.setClickable(true);

            final String myName=curEvents.get(i).getEventName();
            final String myDescription=curEvents.get(i).getDescription();
            final String myLocation=curEvents.get(i).getLocation();
            final String myDateTime=curEvents.get(i).getDateTime();


            event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                    String key=acct.getEmail().replace('.', ',');
                    DatabaseReference mInterestedRef=mRootRef.child("User").child(key).child("Going");

                    Log.d("Key", key);

                    mInterestedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            ArrayList<String> curFollow=new ArrayList<String>();

                            Log.d("checking children", Long.toString(snapshot.getChildrenCount()));

                            for (DataSnapshot datas2: snapshot.getChildren()) {

                                String x=datas2.getValue(String.class);

                                curFollow.add(x);
                            }

                            Intent intent= new Intent(PublishersEvents.this, EventDescription.class);
                            intent.putExtra("eventName",myName);
                            intent.putExtra("eventDescription",myDescription);
                            intent.putExtra("eventLocation",myLocation);
                            intent.putExtra("mylist",curFollow);
                            intent.putExtra("publisherId",identifierVal);
                            intent.putExtra("dateTimeId",myDateTime);
                            startActivity(intent);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            TextView location= new TextView(this);
            location.setText(curEvents.get(i).getLocation());
            location.setWidth(50);
            location.setMaxLines(1);
            location.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
            location.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                location.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }

            TextView date= new TextView(this);
            date.setText(curEvents.get(i).getDateTime());

            date.setWidth(50);
            date.setMaxLines(1);
            date.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
            date.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }

            TextView numAttend=new TextView(this);
            numAttend.setText(Integer.toString(curEvents.get(i).getAttendees()));

            numAttend.setWidth(50);
            numAttend.setMaxLines(1);
            numAttend.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
            numAttend.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                numAttend.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }


            row.addView(event);
            row.addView(location);
            row.addView(date);
            row.addView(numAttend);


            ll.addView(row);
        }
    }

}
