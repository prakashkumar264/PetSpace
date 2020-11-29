package edu.neu.madcourse.petspace.ui.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import edu.neu.madcourse.petspace.MainActivity;
import edu.neu.madcourse.petspace.R;
    public class ForgotPasswordActivity extends AppCompatActivity {

        private EditText ForgotEmail;
        private Button ForgotButton;
        FirebaseAuth mAuth;
        Button cancel_back_to_main;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forgot_password);

            //Initialising Firebase Instance.
            mAuth = FirebaseAuth.getInstance();
            //Defining Views
            ForgotEmail = findViewById(R.id.forgot_email);
            ForgotButton = findViewById(R.id.forgot_button);
            cancel_back_to_main = findViewById(R.id.cancel_back_to_main);
            // Set toolbar title in forgotPasswordActivity screen.
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAbout);
            toolbar.setTitle("Reset Password");
            // Handle Click Events.
            ForgotButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String UserEmail = ForgotEmail.getText().toString().trim();
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!TextUtils.isEmpty(UserEmail)) {
                        mAuth.sendPasswordResetEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ForgotPasswordActivity.this, R.style.AlertDialogCustom));
                                    builder.setMessage("A Password reset link has been sent to your e-mail address!");

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            //Sending User to Login Activity...
                                            Intent loginIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                            startActivity(loginIntent);
                                            dialog.cancel();
                                        }

                                    });

                                    Dialog dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                                    dialog.show();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    }
                }
            });
        }

        //Method to redirect User to MainActivity.
        private void SendUserToMainActivity() {
            Intent forgotIntent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
            forgotIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(forgotIntent);
            finish();
        }

        /**
         * Called when the user taps the Cancel button.
         * @param view the View object that was clicked
         */
        public void onClickCancel(View view) {
            //Sending User to Main or Login depending on which screen they are at.
            Intent loginIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            Toast.makeText(ForgotPasswordActivity.this, "Password Reset Cancelled!", Toast.LENGTH_SHORT).show();
            startActivity(loginIntent);
        }
    }