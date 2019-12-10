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

import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
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

        identifierVal = acct.getEmail();
        final Button home = findViewById(R.id.button3);

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddEventActivity.this, PublisherHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void sendFeedback(View button) {
        // Do click handling here

        //Step 1: Send to confirm details page


//        final EditText nameField = (EditText) findViewById(R.id.editText3);
//        final String name = nameField.getText().toString();
//
//        final EditText descriptionField = (EditText) findViewById(R.id.editText4);
//        final String description = descriptionField.getText().toString();
//
//        Spinner mySpinner = (Spinner) findViewById(R.id.locations_spinner);
//        final String location = mySpinner.getSelectedItem().toString();
//
//
//        final EditText dateField = (EditText) findViewById(R.id.editText6);
//        final String date = dateField.getText().toString();


                final EditText nameField = (EditText) findViewById(R.id.editText3);
                final String name = nameField.getText().toString();

                final EditText descriptionField = (EditText) findViewById(R.id.editText4);
                final String description = descriptionField.getText().toString();

                Spinner mySpinner = (Spinner) findViewById(R.id.locations_spinner);
                final String location = mySpinner.getSelectedItem().toString();


                final EditText dateField = (EditText) findViewById(R.id.editText6);
                final String date = dateField.getText().toString();

                if (validationSuccess(name, description, date)) {

                    Intent intent = new Intent(
                            AddEventActivity.this,
                            ConfirmDetailsActivity.class);
                    intent.putExtra("NAME_ID", name);
                    intent.putExtra("DESCRIPTION_ID", description);
                    intent.putExtra("LOCATION_ID", location);
                    intent.putExtra("DATE_ID", date);
                    startActivity(intent);
                } else {
                    AlertDialog();
                }
    }


    private Boolean validationSuccess(String name, String description, String date) {
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddEventActivity.this);
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

}