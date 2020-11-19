package edu.neu.madcourse.petspace;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import edu.neu.madcourse.petspace.ui.login.ForgotPasswordActivity;
import edu.neu.madcourse.petspace.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    Button forgot_pass_btn, logout_btn;
    private FirebaseAuth mAuth;
    private View post_bar;
    private Toolbar mToolbar;
    private DatabaseReference UserRef, PostRef, LikesRef, CommentRef;
    private String CurrentUserId;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        initializeViews();
        // Display post bar upon loading.
        post_bar = new View(this);
        post_bar.findViewById(R.id.post_bar_included);

        //instantiate Objects
        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        mToolbar = findViewById(R.id.main_page_toolbar);
        setActionBar(mToolbar);
        getActionBar().setIcon(R.drawable.ic_baseline_reorder_24);
        getActionBar().setTitle("PetSpace");

        view = findViewById(R.id.post_bar_included);

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


//        logout_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                mAuth.signOut();
//                startActivity(intent);
//            }
//        });
//        forgot_pass_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
//                    startActivity(intent);
//                }
//        });
    }

    private void initializeViews() {
//        forgot_pass_btn = findViewById(R.id.forgot_pass_btn);
//        logout_btn = findViewById(R.id.logout_btn);
    }
}
