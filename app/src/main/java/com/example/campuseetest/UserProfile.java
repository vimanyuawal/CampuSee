package com.example.campuseetest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.ValueEventListener;

public class UserProfile  extends AppCompatActivity {
    String identifierVal;

    GoogleSignInClient mGoogleSignInClient;
    String name = "";
    String email = "";

    Button sign_out;

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

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        name = acct.getDisplayName();
        email = acct.getEmail();

        final String key = email.replace('.', ',');


        DatabaseReference mNameRef=mRootRef.child("User").child(key).child("name");


        mNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                EditText et = (EditText) findViewById(R.id.plain_text_input);
                et.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        EditText et = (EditText) findViewById(R.id.plain_text_input);
//        et.setText(name);

        sign_out = findViewById(R.id.log_out);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });


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

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserProfile.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserProfile.this, MainActivity.class));
                        finish();
                    }
                });
    }


}
