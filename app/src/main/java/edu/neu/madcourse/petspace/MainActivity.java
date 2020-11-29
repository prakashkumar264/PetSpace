package edu.neu.madcourse.petspace;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

import edu.neu.madcourse.petspace.ui.login.ForgotPasswordActivity;
import edu.neu.madcourse.petspace.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private View post_bar;
    private Toolbar mToolbar;
    private DatabaseReference MessagesRef, HomeRef, UserRef, PostRef, LikesRef, CommentRef;
    private String CurrentUserId;
    private RecyclerView postList;
    View view;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;

    private EditText posttext;
    private ImageButton btn_post, btn_upload;

    //permission constant
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constant
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_STORAGE_CODE = 400;
    //image picked will be stored here
    Uri image_uri;

    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init permissions Arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = MainActivity.this;

        // Get the widgets reference from XML layout
        mToolbar = (Toolbar) findViewById(R.id.toolbarAbout);

        // Set a title for toolbar
//        mToolbar.setTitle("PetSpace");
        //mToolbar.setTitleTextColor(Color.WHITE);
//        getSupportActionBar().setIcon(R.drawable.ic_baseline_reorder_24);
//        getSupportActionBar().setTitle("PetSpace");

        // firebase Instance
        mAuth = FirebaseAuth.getInstance();

        // Display post bar upon loading.
        post_bar = new View(this);
        post_bar.findViewById(R.id.bottom_post_bar);

        //instantiate Objects
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        HomeRef = FirebaseDatabase.getInstance().getReference().child("Home");
        // Menu toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        // Notifications alert
        ImageButton notifications_img_btn = (ImageButton) findViewById(R.id.notifications_image_bttn);
        // Bottom post bar
        view = findViewById(R.id.bottom_post_bar);
        posttext = findViewById(R.id.text_post);
        btn_post = findViewById(R.id.add_new_post_button);
        btn_upload = findViewById(R.id.add_new_upload_button);

        // Set actionbar with toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("");

        // Hide keyboard upon loading main screen.
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Display bottom navigation bar upon loading.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomAppBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:

                        RefreshHome();

                        break;
                    case R.id.Search:

                        SendUserToFindFriendsActivity();

                        break;
                    case R.id.Chat:

                        SendUserToChatForumActivity();

                        break;
                    case R.id.Profile:

                        SendUserToProfileActivity();

                        break;
                    case R.id.Settings:

                        SendUserToSettingsActivity();

                        break;
                }
                return true;
            }
        });
        // handle top nav notifications click event
        notifications_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Notifications", Toast.LENGTH_SHORT).show();

                // test code
                // change notifications image - alert that notifications are pending
                notifications_img_btn.setImageResource(R.drawable.ic_baseline_notifications_active_24);

                }

        });

        //get image
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        //post button click listener
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data
                String content = posttext.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(MainActivity.this, "Enter Text.....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_uri == null){
                    uploadData(content, "noImage");
                }else{
                    uploadData(content, String.valueOf(image_uri));
                }
            }
        });


    }

    private void uploadData(String content, String valueOf) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timestamp;
        if(!valueOf.equals("noImage")){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            storageReference.putFile(Uri.parse(valueOf))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded to firebase storage
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUri = uriTask.getResult().toString();
                            if(uriTask.isSuccessful()){

                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("uid", CurrentUserId);
                                hashMap.put("pcontent", content);
                                hashMap.put("pimage", downloadUri);
                                hashMap.put("ptime", timestamp );

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                                reference.child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity.this, "Post Published", Toast.LENGTH_SHORT).show();
                                                //reset view
                                                posttext.setText("");

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed uploading image
                            Toast.makeText(MainActivity.this, "putting error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{
            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("uid", CurrentUserId);
            hashMap.put("pcontent", content);
            hashMap.put("pimage", "noImage");
            hashMap.put("ptime", timestamp );

            DatabaseReference reference = PostRef;
            reference.child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Post Published", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "no image error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    if(!checkCameraPermission()){

                        requestCameraPermission();
                    }else{
                        Toast.makeText(MainActivity.this, "Pickfromcamer", Toast.LENGTH_SHORT).show();
                        PickFromCamera();
                    }
                }
                if(i == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        Toast.makeText(MainActivity.this, "PickfromGAllery", Toast.LENGTH_SHORT).show();
                        PickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }
    private void PickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_STORAGE_CODE);
    }
    private void PickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }
    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCameraPermission(){
        Toast.makeText(MainActivity.this, "Camera Permission Requested", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        PickFromCamera();
                    }else{
                        Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                    if(grantResults.length>0){
                        boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        if(storageAccepted){
                            PickFromGallery();
                        }else{
                            Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                    }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_STORAGE_CODE){
                image_uri = data.getData();
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:

                RefreshHome();

                break;

            case R.id.Search:

                SendUserToFindFriendsActivity();

                break;

            case R.id.Friend_Requests:

                SendUserToFriendRequestsActivity();

                break;

            case R.id.Notifications:

                SendUserToNotificationsActivity();

                break;

            case R.id.Chat:

                SendUserToChatForumActivity();

                break;
            case R.id.Messages:

                SendUserToMessagingActivity();

                break;
            case R.id.Profile:
                SendUserToMainActivity();

                break;
            case R.id.About:

                 SendUserToAboutActivity();

                break;
            case R.id.Settings:

                SendUserToSettingsActivity();

                break;
            case R.id.Reset_Password:
                Toast.makeText(MainActivity.this, "Reset Password", Toast.LENGTH_SHORT).show();
                SendUserToForgotPasswordActivity();
                break;
            case R.id.Logout:

                Toast.makeText(MainActivity.this, "Logged Out of PetSpace. Come Back Soon!", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();
                SendUserToLoginActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // Method to send user to ProfileSetupActivity screen to edit profile.
    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(MainActivity.this, ProfileSetupActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    // Method for log out and send user to LoginActivity screen.
    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    //Method to redirect User to Forgot Password Activity.
    private void SendUserToForgotPasswordActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, ForgotPasswordActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to Forgot Password Activity.
    private void SendUserToFindFriendsActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, FindFriendsActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to Friend Requests Activity.
    private void SendUserToFriendRequestsActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, FriendRequestsActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to Profile Activity.
    private void SendUserToProfileActivity() {

        Intent forgotIntent = new Intent(MainActivity.this, ProfileActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }


    //Method to redirect User toAbout Activity.
    private void RefreshHome() {

        Intent forgotIntent =new Intent(MainActivity.this, MainActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to SettingsActivity.
    private void SendUserToChatForumActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, ChatForumActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to MessagingActivity.
    private void SendUserToMessagingActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, MessagingActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User toAbout Activity.
    private void SendUserToSettingsActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, SettingsActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to NotificationsActivity.
    private void SendUserToNotificationsActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, NotificationsActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to AboutActivity.
    private void SendUserToAboutActivity() {
        Intent forgotIntent =new Intent(MainActivity.this, AboutActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

}