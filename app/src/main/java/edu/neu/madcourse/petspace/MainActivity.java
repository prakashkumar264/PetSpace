package edu.neu.madcourse.petspace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import edu.neu.madcourse.petspace.ui.login.ForgotPasswordActivity;
import edu.neu.madcourse.petspace.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private View post_bar;
    private Toolbar mToolbar;
    private DatabaseReference UserRef, PostRef, LikesRef, CommentRef;
    private String CurrentUserId;
    private RecyclerView postList;
    View view;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = MainActivity.this;

        // Get the widgets reference from XML layout
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

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
        // Menu toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        // Notifications alert
        ImageButton notifications_img_btn = (ImageButton) findViewById(R.id.notifications_image_bttn);
        // Bottom post bar
        view = findViewById(R.id.bottom_post_bar);

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
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.Search:
                        Toast.makeText(MainActivity.this, "Find Pets", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.Chat:
                        Toast.makeText(MainActivity.this, "Chat", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.Profile:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.Settings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();

                break;

            case R.id.Search:
                Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();

                break;

            case R.id.Friend_Requests:
                Toast.makeText(MainActivity.this, "Friend Requests", Toast.LENGTH_SHORT).show();

                break;

            case R.id.Notifications:
                Toast.makeText(MainActivity.this, "Notifications", Toast.LENGTH_SHORT).show();

                break;

            case R.id.Chat:
                Toast.makeText(MainActivity.this, "Chat", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Messages:
                Toast.makeText(MainActivity.this, "Messages", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Profile:
                SendUserToMainActivity();

                break;
            case R.id.About:
                Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                SendUserToAboutActivity();

                break;
            case R.id.Settings:
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();

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

    //Method to redirect User toAbout Activity.
    private void SendUserToAboutActivity() {

        Intent forgotIntent =new Intent(MainActivity.this, AboutActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

}