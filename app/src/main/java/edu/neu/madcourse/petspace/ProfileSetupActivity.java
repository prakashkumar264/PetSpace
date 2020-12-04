package edu.neu.madcourse.petspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class ProfileSetupActivity extends AppCompatActivity {

    final static int GalleryPic=1;
    FirebaseAuth mAuth;
    private DatabaseReference Users_Ref;
    private StorageReference UserProfileImageRef;
    private Button save_profile_info;
    private EditText user_name, full_name, city_origin, state_origin, country_origin, profile_bio;
    private ImageView Profile_Img;
    private String Current_UserId;
    private ImageButton Add_Photo;
    public static final String Firebase_Server_URL = "https://petspace-2c47c.firebaseio.com/";
    private StorageReference mStorageRef;
    private ImageView img;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        save_profile_info = findViewById(R.id.save_profile_info);
        mAuth = FirebaseAuth.getInstance();

        img=(ImageView)findViewById(R.id.profile_img);
        Current_UserId = mAuth.getCurrentUser().getUid();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Users_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profileImage");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user_name = findViewById(R.id.user_name);
        full_name = findViewById(R.id.full_name);
        city_origin = findViewById(R.id.city_origin);
        state_origin = findViewById(R.id.state_origin);
        country_origin = findViewById(R.id.country_origin);
        profile_bio = findViewById(R.id.profile_bio);
        Profile_Img = findViewById(R.id.profile_img);
        Add_Photo = findViewById(R.id.add_image);

        if (user != null) {
            Uri photoUrl = user.getPhotoUrl();
            Glide.with(this).load(photoUrl).into(img);

        }

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

                ImageView profile_img = (ImageView) findViewById(R.id.profile_img);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

//        String usid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference uidRef = rootRef.child("Users").child(usid);
//        DatabaseReference reference = uidRef.child("profileImage").child(uid);


        Users_Ref.addListenerForSingleValueEvent(eventListener);

        save_profile_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String username = user_name.getText().toString().trim();
                String fullname = full_name.getText().toString().trim();
                String cityorigin =city_origin.getText().toString().trim();
                String stateorigin = state_origin.getText().toString().trim();
                String countryorigin = country_origin.getText().toString().trim();
                String profilebio = profile_bio.getText().toString().trim();

                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter A Valid User Name.",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(fullname))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter Your Full Name.",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(cityorigin))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter A Valid City!",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(stateorigin))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter A Valid State!",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(countryorigin))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter A Valid Country!",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(profilebio))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter A Personal Bio!",Toast.LENGTH_SHORT);
                }
                else
                {

                    HashMap userMap =new HashMap();
                    userMap.put("username",username);
                    userMap.put("fullname",fullname);
                    userMap.put("city",cityorigin);
                    userMap.put("state",stateorigin);
                    userMap.put("country",countryorigin);
                    userMap.put("profilebio",profilebio);

                    Users_Ref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProfileSetupActivity.this,"Account Information Saved!",Toast.LENGTH_LONG);

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();

                                user.updateProfile(profileUpdates);

                                SendUserToMainActivity();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(ProfileSetupActivity.this,message,Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });

        // Open image gallery
        Profile_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPic);
            }
        });

        Users_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("/profileImage")) {
                        String image = dataSnapshot.child("/profileImage").getValue().toString();
                        Picasso.get()
                                .load(image)
                                .placeholder(R.drawable.profile)
                                .into(Profile_Img);
                    }
                }
                else
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Select A Valid Image!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Add_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPic);
            }
        });

        Users_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("/profileImage")) {
                        String image = dataSnapshot.child("/profileImage").getValue().toString();
                        Picasso.get()
                                .load(image)
                                .placeholder(R.drawable.profile)
                                .into(Profile_Img);
                    }
                }
                else
                {
                    Toast.makeText(ProfileSetupActivity.this,"Select an Image..",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== GalleryPic && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        // IMAGE Cropping Source
        // https://stackoverflow.com/questions/15228812/crop-image-in-android
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {

            CropImage.ActivityResult result= CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                Uri resultUri =result.getUri();


                StorageReference filePath = UserProfileImageRef.child(Current_UserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfileSetupActivity.this,"Profile Imaged Saved!",Toast.LENGTH_SHORT).show();

                            // Store Img in FireBase
                            final String downloadUrl = task.getResult().getStorage().toString();

                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference uidRef = rootRef.child("Users").child(uid);
                            uidRef.child("profileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        // Send back to Setup
                                        Intent selfIntent = new Intent(ProfileSetupActivity.this,ProfileSetupActivity.class);
                                        startActivity(selfIntent);
                                        Toast.makeText(ProfileSetupActivity.this, "Profile Image Saved!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        String message =task.getException().getMessage();
                                        Toast.makeText(ProfileSetupActivity.this, "Error. Please Try Again!" +message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }

            else{
                // Image Crop Failure
                Toast.makeText(ProfileSetupActivity.this, "Image Error. Please try again." , Toast.LENGTH_SHORT).show();
            }

        }



    }
    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(ProfileSetupActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    /**
     * Called when the user taps the 'Return to PetSpace' button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        //Sending User back to MainActivity.
        Intent loginIntent = new Intent(ProfileSetupActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }
}

