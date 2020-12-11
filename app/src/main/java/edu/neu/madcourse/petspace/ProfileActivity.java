package edu.neu.madcourse.petspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, PostRef;
    private String CurrentUserId, DatabaseUserId;
    private CircleImageView profileImage;
    private TextView profile_username, profile_fullname, profile_bio;
    private String user_posts, user_followers, user_following;
    private RecyclerView profile_posts_recyclerview;
    private TextView followuser;
    private String CURRENT_STATE;
    private DatabaseReference FriendRequestReference, FollowerReference, FollowerReferenceCurrent;
    private EditText profile_username_edit, profile_fullname_edit, profile_bio_edit;
    private ImageButton user_profile_image_button;
    private FirebaseRecyclerAdapter<UserPosts, UserPostsHolder> firebaseRecyclerAdapter;
    //private ImageView going_back;
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String UserKey = getIntent().getExtras().getString("UserKey");

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        FriendRequestReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserKey).child("FriendRequests");
        FollowerReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserKey).child("Followers");
        FollowerReferenceCurrent = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId).child("Followers");

        profileImage = findViewById(R.id.profile_image);
        profile_username = findViewById(R.id.profile_username);
        profile_fullname = findViewById(R.id.profile_fullname);
        profile_bio = findViewById(R.id.profile_bio);
        profile_posts_recyclerview = findViewById(R.id.profile_posts_rec);
        profile_posts_recyclerview.setHasFixedSize(true);
        profile_posts_recyclerview.setItemViewCacheSize(20);
        profile_posts_recyclerview.setDrawingCacheEnabled(true);
        profile_posts_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        profile_posts_recyclerview.setLayoutManager(gridLayoutManager);

        UserRef.child(UserKey).addValueEventListener(new ValueEventListener() {
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
                        String bio = dataSnapshot.child("profilebio").getValue().toString();
                        profile_bio.setText(bio);
                    }


                } else {
                    Toast.makeText(ProfileActivity.this, "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        DispalyUserPostGrid(UserKey);
    }

    public void DispalyUserPostGrid(final String userKey)
    {
        FirebaseRecyclerOptions<UserPosts> options = new FirebaseRecyclerOptions.Builder<UserPosts>()
                .setQuery(PostRef.orderByChild("uid").equalTo(userKey),UserPosts.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserPosts,UserPostsHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull UserPostsHolder holder, int position, @NonNull UserPosts model) {

                final String PostKey = getRef(position).getKey();
                
                holder.setPostImage(model.postimage);

                holder.mView.findViewById(R.id.user_post_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPostIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        clickPostIntent.putExtra("PostKey", PostKey);
                        startActivity(clickPostIntent);
                    }
                });

            }

            @NonNull
            @Override
            public UserPostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_bar_layout, parent, false);
                return new UserPostsHolder(view);
            }
        };

        profile_posts_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserPostsHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView UserPostImage;

        public UserPostsHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setPostImage(String postImage) {
            UserPostImage = itemView.findViewById(R.id.user_post_image);

            Picasso.get().load(postImage).into(UserPostImage);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBundleRecyclerViewState =new Bundle();
        Parcelable listState = profile_posts_recyclerview.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable("recycler_state",listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mBundleRecyclerViewState != null)
        {

            Parcelable liststate = mBundleRecyclerViewState.getParcelable("recycler_state");
            profile_posts_recyclerview.getLayoutManager().onRestoreInstanceState(liststate);
        }
    }

}