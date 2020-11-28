package edu.neu.madcourse.petspace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EditText user_name = (EditText) findViewById(R.id.user_name);
                user_name.setText(dataSnapshot.child("username").getValue(String.class));

                EditText full_name = (EditText) findViewById(R.id.full_name);
                full_name.setText(dataSnapshot.child("fullname").getValue(String.class));

                EditText city_origin = (EditText) findViewById(R.id.city_origin);
                city_origin.setText(dataSnapshot.child("city").getValue(String.class));

                EditText state_origin = (EditText) findViewById(R.id.state_origin);
                state_origin.setText(dataSnapshot.child("state").getValue(String.class));

                EditText country_origin = (EditText) findViewById(R.id.country_origin);
                country_origin.setText(dataSnapshot.child("country").getValue(String.class));

                EditText profile_bio = (EditText) findViewById(R.id.profile_bio);
                profile_bio.setText(dataSnapshot.child("profilebio").getValue(String.class));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

}