package edu.neu.madcourse.petspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChatForumOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_forum_option);
        Toolbar toolbar = findViewById(R.id.toolbarChatForumOption);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Choose A Chat Forum:");
    }


    public void onClickCatsChat(View view) {
        Intent loginIntent = new Intent(ChatForumOption.this, ChatMessagingActivity.class);
        startActivity(loginIntent);
    }

    public void onClickDogsChat(View view) {
//        Intent loginIntent = new Intent(ChatForumOption.this, MainActivity.class);
//        startActivity(loginIntent);
    }

    /**
     * Called when the user taps the 'Return to PetSpace' button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        //Sending User back to MainActivity.
        Intent loginIntent = new Intent(ChatForumOption.this, MainActivity.class);
        startActivity(loginIntent);
    }

}