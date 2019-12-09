package com.example.campuseetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;

public class AddEventActivity extends AppCompatActivity {

    String identifierVal;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(AddEventActivity.this);

        identifierVal=acct.getEmail();
        final Button home = findViewById(R.id.button3);

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(AddEventActivity.this, PublisherHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void sendFeedback(View button) {
        // Do click handling here

        //Step 1: Send to confirm details page


        final EditText nameField = (EditText) findViewById(R.id.editText3);
        final String name = nameField.getText().toString();

        final EditText descriptionField = (EditText) findViewById(R.id.editText4);
        final String description = descriptionField.getText().toString();

        Spinner mySpinner = (Spinner) findViewById(R.id.locations_spinner);
        final String location = mySpinner.getSelectedItem().toString();


        final EditText dateField = (EditText) findViewById(R.id.editText6);
        final String date = dateField.getText().toString();


        Intent intent = new Intent(AddEventActivity.this, ConfirmDetailsActivity.class);
        intent.putExtra("NAME_ID",name);
        intent.putExtra("DESCRIPTION_ID",description);
        intent.putExtra("LOCATION_ID",location);
        intent.putExtra("DATE_ID",date);
        startActivity(intent);

    }
}