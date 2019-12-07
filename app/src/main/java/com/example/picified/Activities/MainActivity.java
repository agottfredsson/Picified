package com.example.picified.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.picified.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager callbackManager;
    LoginButton btnLoginFacebook;

    EditText userEmail;
    EditText userPassword;
    Button btnLogin;

    String email;
    String password;
    String TAG = "ANTON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        userEmail = findViewById(R.id.edittext_username);
        userPassword = findViewById(R.id.edittext_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginFacebook = findViewById(R.id.btn_login_facebook);

        setupFirebaseAuth();

        mAuth = FirebaseAuth.getInstance();

    }

    @Override // Check if user is signed in and update ui
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        Log.d(TAG, "onStart: User was already signed in, calling homescreen");
    }

    public void txtRegisterUser(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnLoginUser(View view) {
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();

        signInMethod(email, password);
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void signInMethod(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: user signed in");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent (MainActivity.this, FragmentsHandlerActivity.class); //changed
                    startActivity(intent);
                    finish();
                    //update ui user
                } else {
                    Log.d(TAG, "onComplete: user fail to sign in");
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    //update ui null
                }
            }
        });
    }
    private void setupFirebaseAuth() { //sets up a firebase Authentication listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) { //if the the user tries to log in
                FirebaseUser user = firebaseAuth.getCurrentUser(); //if successful a user is asigned else null

                if (user != null) { //if a user was found go to logged in activity
                    Log.d(TAG, "onAuthStateChanged: Signed in" + user.getUid());
                    Toast.makeText(MainActivity.this, "Authentivated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, FragmentsHandlerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
    }
}
