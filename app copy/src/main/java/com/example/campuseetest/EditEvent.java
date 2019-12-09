package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class EditEvent extends AppCompatActivity {

    //event name (needs to be unique)
    String identifierVal;

    //identifier of the publisher AKA datas.getKey()
    String userID;

    String nameC;
    String desC;
    String locC;
    String dateC;

    String key;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(EditEvent.this);

        nameC=getIntent().getStringExtra("eName");
        desC=getIntent().getStringExtra("eDesC");
        locC=getIntent().getStringExtra("eLocC");
        dateC=getIntent().getStringExtra("eDateC");

        key=acct.getEmail().replace('.', ',');

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mEventsRef=mRootRef.child("Publisher").child(key).child("Event").getRef();

        init(nameC,desC,locC,dateC);
    }

    public void init(String curName, String curDescription, String curLocation, String curDateTime){

        //text inside an EditText
        TextView editText1 = (TextView) findViewById(R.id.eventNameField);
        editText1.setText("Event Name= "+curName, TextView.BufferType.EDITABLE);
        editText1.setTextSize(18);

        EditText editText2 = (EditText)findViewById(R.id.eventDescriptionField);
        editText2.setText(curDescription, TextView.BufferType.EDITABLE);

        TextView editText3 = (TextView) findViewById(R.id.eventLocationField);
        editText3.setText("Event Location= "+curLocation, TextView.BufferType.EDITABLE);
        editText3.setTextSize(18);


        EditText editText4 = (EditText)findViewById(R.id.eventDateTimeField);
        editText4.setText(curDateTime, TextView.BufferType.EDITABLE);
    }

    //when you click "Done" button the entries are updated
    public void sendFeedback(View button) {
        // Do click handling here

        //Step 1: Send to confirm details page

        final EditText descriptionField = (EditText) findViewById(R.id.eventDescriptionField);
        String description = descriptionField.getText().toString();

        final EditText DateTimeField = (EditText) findViewById(R.id.eventDateTimeField);
        String dateTime = DateTimeField.getText().toString();

        //update data
        //need to use .setValue
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mEventsRef=mRootRef.child("Publisher").child(key).child("Event").getRef();

        mEventsRef.child(nameC).child("description").setValue(description);
//        mEventsRef.child(nameC).child("location").setValue(location);
        mEventsRef.child(nameC).child("dateTime").setValue(dateTime);

        //redirect to Publisher Home Page
        Intent intent = new Intent(EditEvent.this, PublisherHomeActivity.class);
        startActivity(intent);

    }
}
