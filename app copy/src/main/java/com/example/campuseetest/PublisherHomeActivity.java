package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



import android.view.ViewGroup;
import android.widget.TableRow.LayoutParams;


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
import java.util.List;

public class PublisherHomeActivity extends AppCompatActivity {

    String identifierVal;

    String impKey;

    String finalVal="Does it work: ";

    GoogleSignInClient mGoogleSignInClient;

    String x="test";
    String y="test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_home);

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(PublisherHomeActivity.this);

        identifierVal=acct.getEmail();

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

                    Event current=new Event(datas2.child("eventName").getValue(String.class),datas2.child("description").getValue(String.class),datas2.child("attendees").getValue(int.class),datas2.child("dateTime").getValue(String.class),datas2.child("location").getValue(String.class));

                    curEvents.add(current);
                }

                init(curEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        final Button button = findViewById(R.id.button_Add);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent= new Intent(PublisherHomeActivity.this, AddEventActivity.class);
//                startActivity(intent);
//            }
//        });

        final Button profile = findViewById(R.id.button4);

        profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(PublisherHomeActivity.this, PublisherProfilePage.class);
                startActivity(intent);
            }
        });

        final Button home = findViewById(R.id.button5);

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(PublisherHomeActivity.this, PublisherHomeActivity.class);
                startActivity(intent);
            }
        });

        final Button addevent = findViewById(R.id.button6);

        addevent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(PublisherHomeActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

    }

    public void init(List<Event> curEvents)
    {
        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout1);

        TableRow row= new TableRow(this);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 60);

        TextView eventTitle= new TextView(this);
        eventTitle.setText("Event");
        eventTitle.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        eventTitle.setWidth(50);
        eventTitle.setTextSize(20);
        eventTitle.setPadding(0,2,2,80);
        TextView timeTitle= new TextView(this);
        timeTitle.setText("Location");
        timeTitle.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        timeTitle.setWidth(50);
        timeTitle.setTextSize(20);
        timeTitle.setPadding(2,2,2,80);
        TextView locationTitle= new TextView(this);
        locationTitle.setText("Time");
        locationTitle.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        locationTitle.setWidth(50);
        locationTitle.setTextSize(20);
        locationTitle.setPadding(2,2,2,80);
        TextView attendees= new TextView(this);
        attendees.setText("Attendees");
        attendees.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        attendees.setWidth(50);
        attendees.setTextSize(19);
        attendees.setPadding(2,2,2,80);
        row.addView(eventTitle);
        row.addView(timeTitle);
        row.addView(locationTitle);
        row.addView(attendees);

        ll.addView(row);

        for(int i=0; i < curEvents.size(); i++)
        {

            row= new TableRow(this);
            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 60);
            row.setLayoutParams(lp);


            TextView event= new TextView(this);

            String setterText=curEvents.get(i).getEventName();
            SpannableString content=new SpannableString(setterText);
            content.setSpan(new UnderlineSpan(),0,setterText.length(),0);

            event.setText(content);


            event.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));

            event.setWidth(50);
            event.setMaxLines(1);
            event.setPadding(12,12,12,12);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                event.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }
            event.setClickable(true);

            final String nameC=curEvents.get(i).getEventName();
            final String desC=curEvents.get(i).getDescription();
            final String locC=curEvents.get(i).getLocation();
            final String dateC=curEvents.get(i).getDateTime();


            event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(PublisherHomeActivity.this, EditEvent.class);

                    intent.putExtra("eName",nameC);
                    intent.putExtra("eDesC",desC);
                    intent.putExtra("eLocC",locC);
                    intent.putExtra("eDateC",dateC);

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
            date.setText(curEvents.get(i).getDateTime());

            date.setWidth(50);
            date.setMaxLines(1);
            date.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));

            date.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }

            TextView numAttend= new TextView(this);
            numAttend.setText(Integer.toString(curEvents.get(i).getAttendees()));

            numAttend.setWidth(50);
            numAttend.setMaxLines(1);
            numAttend.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
            numAttend.setPadding(12,12,12,12);

            row.addView(event);
            row.addView(location);
            row.addView(date);
            row.addView(numAttend);

            ll.addView(row);
        }
    }
}