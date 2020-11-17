package edu.neu.madcourse.petspace;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.petspace.ui.login.ForgotPasswordActivity;
import edu.neu.madcourse.petspace.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    Button forgot_pass_btn, logout_btn;
    private FirebaseAuth mAuth;
    private View post_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        initializeViews();

        post_bar = new View(this);
        post_bar.findViewById(R.id.post_bar_included);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                mAuth.signOut();
                startActivity(intent);
            }
        });
        forgot_pass_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                }
        });
    }

    private void initializeViews() {
        forgot_pass_btn = findViewById(R.id.forgot_pass_btn);
        logout_btn = findViewById(R.id.logout_btn);
    }
}
