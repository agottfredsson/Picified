package com.example.picified.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.picified.R;

public class InChatActivity extends AppCompatActivity {

    ImageView userPicture;
    TextView userName;
    Intent intent;
    String username;
    String userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_chat);

        init();

    }
    private void init() {
        userPicture = findViewById(R.id.image_user_picture_inchat);
        userName = findViewById(R.id.text_username_inchat);
        intent = getIntent();
        username = intent.getStringExtra("username");
        userPhoto = intent.getStringExtra("userpicture");
        userName.setText(username);
        Glide.with(this).load(userPhoto).into(userPicture);
    }
}
