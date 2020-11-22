package edu.neu.madcourse.petspace.ui.login;

import android.app.ProgressDialog;
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

import edu.neu.madcourse.petspace.AboutActivity;
import edu.neu.madcourse.petspace.MainActivity;
import edu.neu.madcourse.petspace.ProfileSetupActivity;
import edu.neu.madcourse.petspace.R;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText registerEmail,registerPassword,registerConfirmPassword;
    private Button registerButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        registerEmail = findViewById(R.id.email);
        registerPassword = findViewById(R.id.password);
        registerConfirmPassword = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
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


    private void CreateNewAccount() {

        String email=registerEmail.getText().toString().trim();
        String password=registerPassword.getText().toString().trim();
        String confirmPassword=registerConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {Toast.makeText(this,"Please write your Email",Toast.LENGTH_SHORT).show(); }
        else if(TextUtils.isEmpty(password))
        {Toast.makeText(this,"Please write your Password",Toast.LENGTH_SHORT).show(); }
        else if(TextUtils.isEmpty((confirmPassword)))
        {Toast.makeText(this,"Please Confirm your Password",Toast.LENGTH_SHORT).show(); }
        else if (!password.equals(confirmPassword))
        { Toast.makeText(this,"Your Password Doesn't Match!!!!",Toast.LENGTH_SHORT).show(); }

        else {

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendUserToSetUpActivity();
                                Toast.makeText(SignUpActivity.this, "Registeration Successfully", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    private void SendUserToSetUpActivity() {
        Intent setupIntent = new Intent(SignUpActivity.this,ProfileSetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(SignUpActivity.this,MainActivity.class);
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
        Intent loginIntent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

}
