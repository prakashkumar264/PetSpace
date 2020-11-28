package edu.neu.madcourse.petspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FriendRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
    }

    /**
     * Called when the user taps the 'Return to PetSpace' button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        //Sending User back to MainActivity.
        Intent loginIntent = new Intent(FriendRequestsActivity.this, MainActivity.class);
        startActivity(loginIntent);
    }

}