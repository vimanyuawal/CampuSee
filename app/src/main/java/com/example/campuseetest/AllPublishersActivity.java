package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllPublishersActivity extends AppCompatActivity {

    String identifierVal;

    GoogleSignInClient mGoogleSignInClient;

    DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_publishers);

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(AllPublishersActivity.this);

        identifierVal = acct.getEmail();

        mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mPublisherRef = mRootRef.child("Publisher");


        mPublisherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, String> map
                        = new HashMap<>();
                for (DataSnapshot datas2: snapshot.getChildren()) {

                    String x=datas2.child("name").getValue(String.class);
                    String y=datas2.child("email").getValue(String.class);
                    map.put(x,y);

                }
                    init(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void init(HashMap<String,String> map){

        final ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("mylist");

        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout2);
        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);

        TextView publisherName= new TextView(this);
        publisherName.setText("Publisher Name");
        publisherName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        publisherName.setWidth(50);

        row.addView(publisherName);

        ll.addView(row);



        for (final HashMap.Entry<String,String> entry : map.entrySet())
        {
            row= new TableRow(this);
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);
            row.setLayoutParams(lp);

            TextView publisher= new TextView(this);
            String setterText=entry.getKey();
            SpannableString content=new SpannableString(setterText);
            content.setSpan(new UnderlineSpan(),0,setterText.length(),0);
            publisher.setText(content);


//            TextView publisher= new TextView(this);
//            publisher.setText(entry.getKey());
            publisher.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));

            publisher.setWidth(50);
            publisher.setMaxLines(1);
            publisher.setPadding(12,12,12,12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                publisher.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
            }
            publisher.setClickable(true);

            final String identify=entry.getValue();
            publisher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(AllPublishersActivity.this, PublishersEvents.class);
                    intent.putExtra("publisherId",entry.getValue());
                    startActivity(intent);

                }
            });

            //create whatever follow and unfollow state variable as necessary
            final Button following= new Button(this);
            following.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if(myList.contains(entry.getValue())) {
                following.setText("Unfollow");
//                counter=0;
            }
            else {
                following.setText("Follow");
//                counter=1;
            }

//            final Button unfollowing= new Button(this);
//            unfollowing.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            unfollowing.setText("Unfollow");


            following.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(following.getText().equals("Follow")) {
                        following.setText("Unfollow");
//                        counter=0;


                        String test=entry.getValue();
                        test=test.replace('.', ',');

                        String key=identifierVal.replace('.', ',');

                        String test2=entry.getValue();

                        mRootRef.child("User").child(key).child("Following").child(test).setValue(test2);

                        Context context = getApplicationContext();
                        CharSequence text = "Following "+entry.getKey();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else{
                        following.setText("Follow");
//                        counter=1;


                        String test=entry.getValue();
                        test=test.replace('.', ',');

                        String key=identifierVal.replace('.', ',');

                        String test2=entry.getValue();

                        mRootRef.child("User").child(key).child("Following").child(test).removeValue();

                        Context context = getApplicationContext();
                        CharSequence text = "Unfollowing "+entry.getKey();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }

                }
            });

            row.addView(publisher);
            row.addView(following);


            ll.addView(row);
        }

    }

    public void sendHome(View button) {
        Intent i = new Intent(this, UserHomeActivity.class);
        startActivity(i);
    }
}
