package edu.neu.madcourse.petspace.ui.login;

import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.petspace.MainActivity;
import edu.neu.madcourse.petspace.ProfileSetupActivity;
import edu.neu.madcourse.petspace.R;
import android.content.Intent;
import android.text.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private TextView UserEmail,UserPassword;
    private TextView SignUp,ForgotPassword;
    private static final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        SignUp = findViewById(R.id.register_account_link);
        LoginButton = findViewById(R.id.login_account);
        UserEmail = findViewById(R.id.login_email);
        UserPassword = findViewById(R.id.login_password);

        //Signup Button Listener that redirects to SendUserToRegisterAccount() method......
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterAccount();
            }
        });


        //Login Button Listener that redirects to SendUserToHome() method......
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToHome();
            }
        });

        //Click Listener to The Forgot Password View..
        ForgotPassword = findViewById(R.id.forgot_password);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToForgotPasswordActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToRegisterAccount() {
        Intent registerIntent =new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(registerIntent);

    }
    private void SendUserToHome() {

        String userEmail = UserEmail.getText().toString().trim();
        String userPassword = UserPassword.getText().toString().trim();
        if(TextUtils.isEmpty(userEmail))
        {Toast.makeText(this,"Enter email..",Toast.LENGTH_LONG).show();}
        else if(TextUtils.isEmpty(userPassword))
        {Toast.makeText(this,"Enter password..",Toast.LENGTH_LONG).show();}
        else{


            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {   SendUserToMainActivity();

                                Toast.makeText(LoginActivity.this,"Login Successful. Welcome to PetSpace!",Toast.LENGTH_LONG).show();
                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show(); }
                        }
                    });}
    }

    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent homeIntent = new Intent(LoginActivity.this,LoginActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }
    //Method to redirect User to Forgot Password Activity.
    private void SendUserToForgotPasswordActivity() {

        Intent forgotIntent =new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }

    //Method to redirect User to ProfileSetupActivity.
    private void SendUserToActivityProfileSetup() {

        Intent forgotIntent =new Intent(LoginActivity.this, ProfileSetupActivity.class);
        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forgotIntent);
        finish();
    }
}
