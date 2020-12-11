package edu.neu.madcourse.petspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,PostRef;
    private String CurrentUserId,DatabaseUserId;
    private CircleImageView profileImage;
    private TextView profile_username,profile_fullname,profile_bio;
    private String user_posts,user_followers,user_following;
    private RecyclerView profile_posts_recyclerview;
    private TextView followuser;
    private DatabaseReference FriendRequestReference,FollowerReference,FollowerReferenceCurrent;
    private EditText profile_username_edit,profile_fullname_edit,profile_bio_edit;
    private ImageButton user_profile_image_button;
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String UserKey = getIntent().getExtras().getString("UserKey");

        //Instantiating Firebase Objects.
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        FriendRequestReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserKey).child("FriendRequests");
        FollowerReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserKey).child("Followers");
        FollowerReferenceCurrent = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId).child("Followers");

        // ProfileActivity Widgets
        profileImage = findViewById(R.id.profile_image);

        profile_username = findViewById(R.id.profile_username);


        profile_fullname = findViewById(R.id.profile_fullname);


        profile_bio = findViewById(R.id.profile_bio);


        followuser = findViewById(R.id.user_total_followers);


        profile_posts_recyclerview = findViewById(R.id.profile_posts_rec);
        profile_posts_recyclerview.setHasFixedSize(true);
        profile_posts_recyclerview.setItemViewCacheSize(20);
        profile_posts_recyclerview.setDrawingCacheEnabled(true);
        profile_posts_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        profile_posts_recyclerview.setLayoutManager(gridLayoutManager);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("profileImage")) {
                        String profileimage = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(profileImage);

                    }

                    if (dataSnapshot.hasChild("fullname")) {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        profile_fullname.setText(fullname);

                    }

                    if (dataSnapshot.hasChild("username")) {
                        String username = dataSnapshot.child("username").getValue().toString();
                        profile_username.setText("@" + username);

                    }

                    if (dataSnapshot.hasChild("profilebio")) {
                        String profile = dataSnapshot.child("username").getValue().toString();
                        profile_bio.setText(profile);

                    }


//                    EditText user_name = (EditText) findViewById(R.id.user_name);
//                    user_name.setText(dataSnapshot.child("username").getValue(String.class));
//
//                    EditText full_name = (EditText) findViewById(R.id.full_name);
//                    full_name.setText(dataSnapshot.child("fullname").getValue(String.class));
//
//                    EditText city_origin = (EditText) findViewById(R.id.city_origin);
//                    city_origin.setText(dataSnapshot.child("city").getValue(String.class));
//
//                    EditText state_origin = (EditText) findViewById(R.id.state_origin);
//                    state_origin.setText(dataSnapshot.child("state").getValue(String.class));
//
//                    EditText country_origin = (EditText) findViewById(R.id.country_origin);
//                    country_origin.setText(dataSnapshot.child("country").getValue(String.class));
//
//                    EditText profile_bio = (EditText) findViewById(R.id.profile_bio);
//                    profile_bio.setText(dataSnapshot.child("profilebio").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        }


    public static class ProfileViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public ProfileViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String fullname)
        {
            TextView name = mView.findViewById(R.id.profile_fullname);
            name.setText(fullname);
        }
        public void setProfileImage(String profileImage)
        {
            CircleImageView imageView = mView.findViewById(R.id.profile_image);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(imageView);
        }
        public void setUsername(String username)
        {
            TextView name = mView.findViewById(R.id.profile_username);
            name.setText(username);

        }
    }
}