package edu.neu.madcourse.petspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivity extends AppCompatActivity {

    private EditText SearchInputText;
    private RecyclerView SearchList;
    private ImageButton Back;
    private DatabaseReference AllUsersRef;
    private String CurrentUserId;
    private FirebaseAuth mAuth;
    private ImageButton SearchFriendsButton;

    // With help from the tutorial:
    //
    // https://sdk6.getsocial.im/guides/notifications/usecases/user-to-user-notifications

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        AllUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        SearchList = findViewById(R.id.find_friends_list);
        SearchList.setHasFixedSize(true);
        SearchList.setLayoutManager(new LinearLayoutManager(this));

        SearchInputText = findViewById(R.id.search_friends_text);
        Back = findViewById(R.id.back_button);
        SearchFriendsButton = findViewById(R.id.search_friends_button);

        SearchFriends("");

        SearchFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchBoxInput =SearchInputText.getText().toString();

                // Hide Keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                SearchFriends(searchBoxInput);

            }
        });

    }

    private void SearchFriends(String searchBoxInput) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(uid);
        DatabaseReference AllUsersRef = uidRef.child("fullname").child(uid);

        FirebaseRecyclerOptions<FindFriendsObject> options =
                new FirebaseRecyclerOptions.Builder<FindFriendsObject>()
                        .setQuery(AllUsersRef.orderByChild("fullname").startAt(searchBoxInput).endAt(searchBoxInput+"\uf8ff"),
                                FindFriendsObject.class)
                        .build();

        FirebaseRecyclerAdapter<FindFriendsObject,FindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindFriendsObject, FindFriendsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull FindFriendsObject model) {
                // Bind data to RecyclerView
                final String UserKey = getRef(position).getKey();

                holder.setFullname(model.fullname);
                holder.setUsername("@"+model.username);
                holder.setProfileImage(model.profileImage);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FindFriendsActivity.this,"Open Profile",Toast.LENGTH_SHORT).show();
                        SendUserToProfileActivity(UserKey);

                    }
                });

            }

            @NonNull
            @Override
            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view  = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_profile,parent,false);

                return new FindFriendsViewHolder(view);
            }
        };

        //Setting RecyclerView Adapter
        SearchList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FindFriendsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String fullname)
        {
            TextView name = mView.findViewById(R.id.full_name);
            name.setText(fullname);
        }
        public void setProfileImage(String profileImage)
        {
            CircleImageView imageView = mView.findViewById(R.id.profile_img);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(imageView);
        }
        public void setUsername(String username)
        {
            TextView name = mView.findViewById(R.id.user_name);
            name.setText(username);

        }
    }
    public void SendUserToProfileActivity(String UserKey)
    {
        Intent sendProfile = new Intent(FindFriendsActivity.this,ProfileActivity.class);
        sendProfile.putExtra("UserKey",UserKey);
        startActivity(sendProfile);

    }

    /**
     * Called when the user taps the 'Return to PetSpace' button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        //Sending User back to MainActivity.
        Intent loginIntent = new Intent(FindFriendsActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

}

