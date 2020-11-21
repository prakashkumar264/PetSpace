package edu.neu.madcourse.petspace;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import edu.neu.madcourse.petspace.ui.login.LoginActivity;
import edu.neu.madcourse.petspace.ui.login.SignUpActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * Called when the user taps the 'Return to PetSpace' button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        //Sending User back to MainActivity.
        Intent loginIntent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }
}