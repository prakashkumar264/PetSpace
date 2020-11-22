package edu.neu.madcourse.petspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;


public class ProfileSetupActivity extends AppCompatActivity {

    final static int GalleryPic=1;
    FirebaseAuth mAuth;
    private DatabaseReference Users_Ref;
    private StorageReference UserProfileImageRef;
    private Button save_profile_info;
    private EditText user_name, full_name, country_origin;
    private ImageView Profile_Img;
    private String Current_UserId;
    private ImageButton Add_Photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        save_profile_info = findViewById(R.id.save_profile_info);
        mAuth = FirebaseAuth.getInstance();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        Current_UserId = mAuth.getCurrentUser().getUid();
        Users_Ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_UserId);
        user_name = findViewById(R.id.user_name);
        full_name = findViewById(R.id.full_name);
        country_origin = findViewById(R.id.country_origin);
        Profile_Img = findViewById(R.id.profile_img);
        Add_Photo = findViewById(R.id.add_image);
        save_profile_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String username = user_name.getText().toString().trim();
                String fullname = full_name.getText().toString().trim();
                String countryorigin = country_origin.getText().toString().trim();

                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter A Valid User Name.",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(fullname))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter Your Full Name.",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(countryorigin))
                {
                    Toast.makeText(ProfileSetupActivity.this,"Please Enter A Valid Country of Origin.",Toast.LENGTH_SHORT);
                }
                else
                {

                    HashMap userMap =new HashMap();
                    userMap.put("username",username);
                    userMap.put("fullname",fullname);
                    userMap.put("country",countryorigin);

                    Users_Ref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {   SendUserToMainActivity();
                                Toast.makeText(ProfileSetupActivity.this,"Your Account has been Created Successfully",Toast.LENGTH_LONG);

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
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("profileImage")) {
                        String image = dataSnapshot.child("profileImage").getValue().toString();
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
                    if(dataSnapshot.hasChild("profileImage")) {
                        String image = dataSnapshot.child("profileImage").getValue().toString();
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
                    .setCropShape(CropImageView.CropShape.OVAL)
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
                            Users_Ref.child("profileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
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
