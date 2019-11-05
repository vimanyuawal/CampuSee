package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class UserHomeActivity extends AppCompatActivity {

    String identifierVal;

    GoogleSignInClient mGoogleSignInClient;

//    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

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
    }
}
