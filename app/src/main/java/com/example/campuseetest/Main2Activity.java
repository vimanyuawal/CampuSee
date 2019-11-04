package com.example.campuseetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

public class Main2Activity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    Button sign_out;
    TextView nameTV;
    TextView emailTV;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("User");
    DatabaseReference mPublisherRef = mRootRef.child("Publisher");

    String emailKey = "";
    String nameKey = "";

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FirebaseApp.initializeApp(this);

        sign_out = findViewById(R.id.log_out);
        nameTV = findViewById(R.id.name);
        emailTV = findViewById(R.id.email);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Main2Activity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            nameTV.setText("Name: "+personName);
            emailTV.setText("Email: "+personEmail);

            this.emailKey = personEmail;
            this.nameKey = personName;
        }

        addListenerOnButton();

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    public void addListenerOnButton() {

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(Main2Activity.this,
                        radioSexButton.getText(), Toast.LENGTH_SHORT).show();



                saveToDatabase(radioSexButton.getText().toString());



                //redirect to user home page if user


                //redirect to publisher home page if publisher
                if(radioSexButton.getText().toString().equals("Publisher")){
                    Intent intent = new Intent(Main2Activity.this, PublisherHomeActivity.class);
                    startActivity(intent);
                }
                else if(radioSexButton.getText().toString().equals("User")){
                    Intent intent = new Intent(Main2Activity.this, UserHomeActivity.class);
                    startActivity(intent);
                }

            }

        });

    }

    public void saveToDatabase(String access){
//        emailKey = emailKey.replace('.', ',');

        if(access.equals("User")) {
            final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

            Query queryToGetData = dbRef.child("User").orderByChild("name").equalTo(nameKey);

            queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        String userId = dbRef.child("Publisher").push().getKey();
                        User user = new User(nameKey, emailKey);
                        dbRef.child("User").child(userId).setValue(user);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else if(access.equals("Publisher")) {

            final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

            Query queryToGetData = dbRef.child("Publisher").orderByChild("name").equalTo(nameKey);

            queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        String userId = dbRef.child("Publisher").push().getKey();
                        Publisher publisher = new Publisher(nameKey, emailKey);
                        dbRef.child("Publisher").child(userId).setValue(publisher);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        else{
            Log.d("a", "FUCKED!!!!!!!!!!!!!!!!!!!!!!_"+ access);
        }

        Log.d("a","Data has been sent to database");
    }


    @Override
    public void onStart() {
        super.onStart();



    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Main2Activity.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Main2Activity.this, MainActivity.class));
                        finish();
                    }
                });
    }
}
