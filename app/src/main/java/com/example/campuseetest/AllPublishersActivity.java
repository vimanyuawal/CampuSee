package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

public class AllPublishersActivity extends AppCompatActivity {

    String identifierVal;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_publishers);

        identifierVal = getIntent().getStringExtra("identifier");

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(AllPublishersActivity.this);

        identifierVal = acct.getDisplayName();

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mPublisherRef = mRootRef.child("Publisher");

//        mPublisherRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                List<Publisher> curEvents=new ArrayList<Publisher>();
//                for (DataSnapshot datas2: snapshot.getChildren()) {
//                    Publisher current=new Event(datas2.child("eventName").getValue(String.class),datas2.child("description").getValue(String.class),datas2.child("attendees").getValue(int.class),null,datas2.child("location").getValue(String.class));
//
//                    curEvents.add(current);
//                }
//
//                init(curEvents);
//
////                            TextView showName=(TextView)findViewById(R.id.nameShowing);
////                            showName.setText("Name: "+xyz);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//

    }
}
