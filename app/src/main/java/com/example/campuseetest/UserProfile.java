package com.example.campuseetest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile  extends AppCompatActivity {
    String identifierVal;

    GoogleSignInClient mGoogleSignInClient;
    String name = "";
    String email = "";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("User");

//    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(UserProfile.this);

        name = acct.getDisplayName();
        email = acct.getEmail();

        final String key = email.replace('.', ',');

        EditText et = (EditText) findViewById(R.id.plain_text_input);
        et.setText(name);


        Button button2=findViewById(R.id.commit_profile_change);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText n = (EditText) findViewById(R.id.plain_text_input);
                String newName = n.getText().toString();

                mUserRef.child(key).child("name").setValue(newName);

                Intent intent= new Intent(UserProfile.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

    }




}
