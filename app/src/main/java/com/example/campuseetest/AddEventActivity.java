package com.example.campuseetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddEventActivity extends AppCompatActivity {

    String identifierVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        identifierVal=getIntent().getStringExtra("identifier");
    }

    public void sendFeedback(View button) {
        // Do click handling here

        //Step 1: Send to confirm details page

        final EditText nameField = (EditText) findViewById(R.id.eventNameField);
        String name = nameField.getText().toString();

        final EditText descriptionField = (EditText) findViewById(R.id.eventDescriptionField);
        String description = descriptionField.getText().toString();

        final EditText LocationField = (EditText) findViewById(R.id.eventLocationField);
        String location = LocationField.getText().toString();

        Intent intent = new Intent(AddEventActivity.this, ConfirmDetailsActivity.class);
        intent.putExtra("NAME_ID",name);
        intent.putExtra("DESCRIPTION_ID",description);
        intent.putExtra("LOCATION_ID",location);
        intent.putExtra("identifier",identifierVal);
        startActivity(intent);
    }
}
