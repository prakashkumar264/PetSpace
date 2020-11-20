package edu.neu.madcourse.petspace;

import android.app.Activity;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Toast;


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

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);

        ImageButton notifications_img_btn = (ImageButton) findViewById(R.id.notifications_image_bttn);

//        getSupportActionBar().setIcon(R.drawable.ic_baseline_reorder_24);
//        getSupportActionBar().setTitle("PetSpace");

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
            case R.id.New_Post:
                Toast.makeText(MainActivity.this, "New Post", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Chat:
                Toast.makeText(MainActivity.this, "Chat", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Messages:
                Toast.makeText(MainActivity.this, "Messages", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Profile:
                Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Settings:
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Reset_Password:
                Toast.makeText(MainActivity.this, "Reset Password", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Logout:
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



}