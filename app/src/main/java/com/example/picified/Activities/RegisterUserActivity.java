package com.example.picified.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.picified.Fragments.ProfileFragment;
import com.example.picified.R;
import com.example.picified.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText userEmail;
    EditText userPassword;
    Button btnRegister;


    String TAG = "ANTON";

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.edittext_register_email);
        userPassword = findViewById(R.id.edittext_register_password);
        btnRegister = findViewById(R.id.btn_register_user);




    }

    private void registerUser (final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterUserActivity.this, "User created", Toast.LENGTH_SHORT).show();

                    User user = new User();
                    user.setName(email.substring(0,email.indexOf("@")));
                    user.setEmail(email);
                    user.setProfile_picture("https://www.cfdating.com/user_images/default.png");
                    user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: User registered to database");
                            } else {
                                Log.d(TAG, "onComplete: User failed to register to database");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: couldn't add user" + e.toString());
                        }
                    });

                    FirebaseAuth.getInstance().signOut();
                    redirectToLoginScreen();

                    //update ui user
                } else {
                    Toast.makeText(RegisterUserActivity.this, "Creation failed", Toast.LENGTH_SHORT).show();
                    // update ui user = null
                }
            }
        });
    }

    public void btnRegisterUser(View view) {
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();

        registerUser(email, password);
        userEmail.setText("");
        userPassword.setText("");
    }

    private void redirectToLoginScreen() {
        Intent intent = new Intent (RegisterUserActivity.this, FragmentsHandlerActivity.class);
        startActivity(intent);
        finish();
    }
}
