package com.example.picified.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.picified.R;

public class PictureViewActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);

        Intent intent = getIntent();
        String b = intent.getStringExtra("a");
        textView = findViewById(R.id.text_test);
        textView.setText(b);

    }
}
