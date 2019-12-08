package com.example.picified.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    EditText userEmail, userPassword, userConfirmPassword;
    Button btnRegister;
    ProgressBar progressBar;


    String TAG = "ANTON";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        init();




    }

    //Method to initiate all variables and views.
    private void init (){
        mAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.edittext_register_email);
        userPassword = findViewById(R.id.edittext_register_password);
        userConfirmPassword = findViewById(R.id.edittext_confirm_password);
        btnRegister = findViewById(R.id.btn_register_user);
        progressBar = findViewById(R.id.progressBar);
    }

    //Method to register-user to Auth and Database.
    private void registerUser(final String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterUserActivity.this, "User created", Toast.LENGTH_SHORT).show();
                    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //adds user to database
                    User user = new User();
                    user.setName(email.substring(0,email.indexOf("@")));
                    user.setEmail(email);
                    user.setProfile_picture("https://www.cfdating.com/user_images/default.png");
                    user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                  /*  user.setBio("");
                    user.setAge("");
                    user.setFrom("");
                    user.setGender(""); Fix this later maybe. */

                    FirebaseDatabase.
                            getInstance().
                            getReference().
                            child(getString(R.string.db_users)).
                            child(FirebaseAuth.getInstance().getUid()).
                            setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d(TAG, "onComplete: added user to database");
                            }
                            else
                            {
                                Log.d(TAG, "onComplete: added user to database");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: couldn't add user" + e.toString());
                        }
                    });
                    //end of adding user to database


                    FirebaseAuth.getInstance().signOut();
                    redirectLoginScreen();
                } else {
                    Toast.makeText(RegisterUserActivity.this, getText(R.string.unable_register_toast), Toast.LENGTH_SHORT).show();
                }
                //hideProgress(); Fix this later
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("Jesper", "Failed Registration: " + e.getMessage());
            }
        });
    }

    //Method that shows progressbar when register.
    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    //Onclick listener on register-user button.
    public void btnRegisterUser(View view) {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confPassword = userConfirmPassword.getText().toString();

        if (!isEmpty(email) && !isEmpty(password) && !isEmpty(confPassword)) {
            if (password.length() >= 6 && confPassword.length() >= 6) {

                if (isValidEmail(email)) {
                    if (isMatchingPass(password, confPassword)) {
                        showProgress();
                        registerUser(email, password);

                    } else {
                        Toast.makeText(this, getText(R.string.password_not_match_toast), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getText(R.string.invalid_email_txt), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.too_short_password_toast), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.all_field_toast), Toast.LENGTH_SHORT).show();
        }
    }

    //Method that checks if string is empty.
    private Boolean isEmpty(String text) {
        return (text.equals(""));
    }

    //Method that controls that user typing a email-adress.
    private Boolean isValidEmail(String email) {
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@' && (email.contains(".com") || email.contains(".se"))) {
                return true;
            }
        }
        return false;
    } //att fixa

    //Method that controls that both password are the same.
    private Boolean isMatchingPass(String password, String confirmedPassword) {
        return password.equals(confirmedPassword);
    }

    //Sends the user to login screen.
    private void redirectLoginScreen() {
        Intent intent = new Intent (RegisterUserActivity.this, FragmentsHandlerActivity.class);
        startActivity(intent);
        finish();
    }
}

