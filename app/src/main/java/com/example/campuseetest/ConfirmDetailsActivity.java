package com.example.campuseetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ConfirmDetailsActivity extends AppCompatActivity {

    String nameId="";
    String descriptionId="";
    String locationId="";
    String identifierVal="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        nameId = getIntent().getStringExtra("NAME_ID");
        descriptionId = getIntent().getStringExtra("DESCRIPTION_ID");
        locationId = getIntent().getStringExtra("LOCATION_ID");
        identifierVal=getIntent().getStringExtra("identifier");

        TextView showName=(TextView)findViewById(R.id.nameShowing);
        showName.setText("Event Name: "+nameId);

        TextView showDes=(TextView)findViewById(R.id.descriptionShowing);
        showDes.setText("Event Description: "+descriptionId);

        TextView showLoc=(TextView)findViewById(R.id.locationShowing);
        showLoc.setText("Event Location: "+locationId);
    }

    public void sendFeedback(View button) {
        Intent intent = new Intent(ConfirmDetailsActivity.this, AddEventActivity.class);
        startActivity(intent);
    }

    public void sendAddition(View button) {

        final Event catchEvent=new Event(nameId,descriptionId,0,null,locationId);

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mPublisherRef = mRootRef.child("Publisher");

        mPublisherRef.orderByChild("name").equalTo(identifierVal).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas: dataSnapshot.getChildren()) {

                    //datas.getKey()
                    String eventID = mRootRef.child("Publisher").child(datas.getKey()).child("Event").push().getKey();
                    mRootRef.child("Publisher").child(datas.getKey()).child("Event").child(eventID).setValue(catchEvent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });











        Intent intent = new Intent(ConfirmDetailsActivity.this, PublisherHomeActivity.class);
//        intent.putExtra("identifier",identifierVal);
        startActivity(intent);


    }


}
