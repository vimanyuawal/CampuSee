package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity {

    String identifierVal;

    GoogleSignInClient mGoogleSignInClient;

    List<Event> curEvents=new ArrayList<Event>();

    List<String> followingX=new ArrayList<String>();

    TableLayout ll;
    TableRow.LayoutParams lp;

    TableRow row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);



        ll = (TableLayout) findViewById(R.id.tableLayout1);

        row= new TableRow(this);
        lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);

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




        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(UserHomeActivity.this);

        identifierVal=acct.getEmail();

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final String key=identifierVal.replace('.', ',');

        DatabaseReference mFollowRef=mRootRef.child("User").child(key).child("Following").getRef();

        Log.d("TAG","User key is: "+key);

        DatabaseReference user = mRootRef.child("User").child(key).child("Following");
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot datas: dataSnapshot.getChildren()) {
                        followingX.add(datas.getValue(String.class));
                }

                Log.d("TAG","Number of following "+followingX.size());

                for(int i=0;i<followingX.size();++i){

                    String sample=followingX.get(i);
                    String fixKey=sample.replace('.',',');
                    DatabaseReference mEventsRef=mRootRef.child("Publisher").child(fixKey).child("Event").getRef();

                    mEventsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot datas2: snapshot.getChildren()) {

                                Event current=new Event(datas2.child("eventName").getValue(String.class),datas2.child("description").getValue(String.class),datas2.child("attendees").getValue(int.class),datas2.child("dateTime").getValue(String.class),datas2.child("location").getValue(String.class));

                                curEvents.add(current);


                            }

                            init(curEvents);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button button2=findViewById(R.id.my_publishers_id);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(UserHomeActivity.this, PubsIFollow.class);
                startActivity(intent);
            }
        });

        Button button3=findViewById(R.id.edit_user);

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(UserHomeActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });

        //set a button to see all publishers
        final Button button = findViewById(R.id.publishers_id);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                String key=identifierVal.replace('.', ',');
                DatabaseReference mFollowersRef=mRootRef.child("User").child(key).child("Following").getRef();

                mFollowersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String xyz="";

                        ArrayList<String> curFollow=new ArrayList<String>();
                        for (DataSnapshot datas2: snapshot.getChildren()) {

                            String x=datas2.getValue(String.class);

                            curFollow.add(x);
                        }

                        Intent intent= new Intent(UserHomeActivity.this, AllPublishersActivity.class);

                        intent.putExtra("mylist",curFollow);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        Button notifs = findViewById(R.id.notifcations);
        notifs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(UserHomeActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        Button map = findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(UserHomeActivity.this, Map.class);
                Log.i("a", "We here bitches");
                startActivity(intent);
            }
        });

    }

    public void init(List<Event> curEvents)
    {
        Log.d("TAG","INIT being called");



        for(int i=0; i < curEvents.size(); i++)
        {

            row= new TableRow(this);
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);
            row.setLayoutParams(lp);


            TextView event= new TextView(this);

//            String setterText=curEvents.get(i).getEventName();
//            SpannableString content=new SpannableString(setterText);
//            content.setSpan(new UnderlineSpan(),0,setterText.length(),0);

            event.setText(curEvents.get(i).getEventName());


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
//                    Intent intent= new Intent(UserHomeActivity.this, EventDescription.class);
//
//                    intent.putExtra("eventName",myName);
//                    intent.putExtra("eventDescription",myDescription);
//                    intent.putExtra("eventLocation",myLocation);
//                    intent.putExtra("mylist",curFollow);
//                    intent.putExtra("publisherId",identifierVal);
//                    intent.putExtra("dateTimeId",myDateTime);
//
//                    startActivity(intent);

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

            TextView numAttend= new TextView(this);
            numAttend.setText(Integer.toString(curEvents.get(i).getAttendees()));

            numAttend.setWidth(50);
            numAttend.setMaxLines(1);
            numAttend.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
            numAttend.setPadding(12,12,12,12);

            row.addView(event);
            row.addView(location);
            row.addView(date);
            row.addView(numAttend);

            ll.addView(row);
        }
    }

    public boolean checkAdd(){
        return true;
    }
}
