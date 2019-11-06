package com.example.campuseetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PublisherProfilePage extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mPubRef = mRootRef.child("Publisher");
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_profile_page);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(PublisherProfilePage.this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        TextView name = (TextView) findViewById(R.id.editText);
        TextView email= (TextView) findViewById(R.id.editText2);

        key = acct.getEmail().replace('.', ',');


        name.setText(acct.getDisplayName());
        email.setText(acct.getEmail());

        final Button profile = findViewById(R.id.button4);

        profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(PublisherProfilePage.this, PublisherProfilePage.class);
                startActivity(intent);
            }
        });

        final Button home = findViewById(R.id.button5);

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(PublisherProfilePage.this, PublisherHomeActivity.class);
                startActivity(intent);
            }
        });

        final Button addevent = findViewById(R.id.button6);

        addevent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(PublisherProfilePage.this, AddEventActivity.class);
                startActivity(intent);

            }
        });

        final Button editEvent = findViewById(R.id.button2);

        editEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(PublisherProfilePage.this, PublisherHomeActivity.class);
                EditText n = (EditText) findViewById(R.id.editText);
                String newName = n.getText().toString();

                mPubRef.child(key).child("name").setValue(newName);
                startActivity(intent);

            }
        });
        
    }

}
