package edu.neu.madcourse.petspace.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import edu.neu.madcourse.petspace.MainActivity;
import edu.neu.madcourse.petspace.R;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

    public class SignUpActivity extends AppCompatActivity {

        private EditText emailEt, passwordTV;
        private Button regBtn;

        private FirebaseAuth mAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

            mAuth = FirebaseAuth.getInstance();

            initializeUI();

            regBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerNewUser();
                }
            });
        }

        private void registerNewUser() {

            String email, password;
            email = emailEt.getText().toString();
            password = passwordTV.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Please enter a valid e-mail address.", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Please enter a valid password.", Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration Successful! Welcome to PetSpace!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Registration failed! Please try again later.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        private void initializeUI() {
            emailEt = findViewById(R.id.email);
            passwordTV = findViewById(R.id.password);
            regBtn = findViewById(R.id.register);
        }

        /**
         * Called when the user taps the Cancel button.
         * @param view the View object that was clicked
         */
        public void onClickCancel(View view) {
            //Sending User to Main or Login depending on which screen they are at.
            Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
            Toast.makeText(SignUpActivity.this, "Registration Cancelled!", Toast.LENGTH_SHORT).show();
            startActivity(loginIntent);
        }

    }