package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        //start added changes
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText descriptionField = (EditText) findViewById(R.id.eventDescriptionField);
                String description = descriptionField.getText().toString();

                final EditText DateTimeField = (EditText) findViewById(R.id.eventDateTimeField);
                String dateTime = DateTimeField.getText().toString();

                if (validationSuccess( description, dateTime)) {
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                    DatabaseReference mEventsRef=mRootRef.child("Publisher").child(key).child("Event").getRef();

                    mEventsRef.child(nameC).child("description").setValue(description);
//        mEventsRef.child(nameC).child("location").setValue(location);
                    mEventsRef.child(nameC).child("dateTime").setValue(dateTime);
                    Intent intent = new Intent(EditEvent.this, PublisherHomeActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog();
                }
            }
        });


        //end changes




    }

    //started changes
    private Boolean validationSuccess(String description, String date) {


        if (description.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter description", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (date.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(validateJavaDate(date) == false)
        {
            Toast.makeText(getApplicationContext(), "Please enter proper date format", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void AlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditEvent.this);
        alertDialogBuilder.setMessage("Oops, there appears to have been an invalid input. Please try again.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    public static boolean validateJavaDate(String strDate)
    {

        /*
         * Set preferred date format,
         * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        sdfrmt.setLenient(false);
        /* Create Date object
         * parse the string into date
         */
        try
        {
            Date javaDate = sdfrmt.parse(strDate);

        }
        /* Date format is invalid */
        catch (ParseException e)
        {

            return false;
        }
        /* Return true if date format is valid */
        return true;

    }
    //changes ended

}
